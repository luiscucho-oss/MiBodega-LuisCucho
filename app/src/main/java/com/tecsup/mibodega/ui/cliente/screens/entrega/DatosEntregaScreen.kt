package com.tecsup.mibodega.ui.cliente.screens.entrega

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.CampoTexto
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.VerdeBodega

/**
 * Pantalla 6: Datos de entrega (mockup "Cliente").
 * Se abre desde el carrito con "Continuar pedido".
 * No navega sola ni toca el carrito: los montos llegan ya calculados
 * desde ClienteApp y los botones solo avisan hacia arriba (state hoisting).
 *
 * @param subtotal suma de (precio x cantidad) de todo el carrito
 * @param delivery costo fijo del delivery
 * @param total subtotal + delivery
 * @param onConfirmarPedido recibe los datos ya validados (ningún campo vacío)
 */
@OptIn(ExperimentalMaterial3Api::class) // TopAppBar todavía es experimental en Material 3
@Composable
fun DatosEntregaScreen(
    subtotal: Double,
    delivery: Double,
    total: Double,
    onVolver: () -> Unit,
    onConfirmarPedido: (nombre: String, telefono: String, direccion: String, referencia: String) -> Unit
) {
    // Estado del formulario: vive aquí con remember (igual que en RegistroScreen)
    // porque solo esta pantalla necesita los textos mientras el usuario escribe.
    // "by" permite leer y escribir la variable como si fuera un String normal.
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var referencia by remember { mutableStateOf("") }

    // El botón solo se activa si los 4 campos tienen texto. Se usa isNotBlank()
    // (y no isNotEmpty()) para que un campo con solo espacios cuente como vacío.
    // Como se calcula en cada recomposición, se actualiza solo al escribir.
    val camposCompletos = listOf(nombre, telefono, direccion, referencia).all { it.isNotBlank() }

    Scaffold(
        // safeDrawing incluye las barras del sistema y también el teclado:
        // así, cuando el teclado se abre, el contenido no queda tapado.
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            // Barra superior con la flecha para volver y el título de la pantalla
            TopAppBar(
                title = { Text("Datos de entrega", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        // Versión AutoMirrored: la flecha se voltea sola en idiomas
                        // que se leen de derecha a izquierda (y no está deprecada)
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingInterno ->
        // paddingInterno = espacio que ocupa la TopAppBar (y las barras del sistema).
        // Se aplica primero para que el contenido empiece debajo de la barra.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
                // verticalScroll: si el teclado ocupa media pantalla, se puede
                // seguir bajando para ver el resto de campos
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "¿A dónde llevamos tu pedido?",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp, bottom = 20.dp)
            )

            FormularioEntrega(
                nombre = nombre,
                onNombreCambia = { nombre = it },
                telefono = telefono,
                onTelefonoCambia = { telefono = it },
                direccion = direccion,
                onDireccionCambia = { direccion = it },
                referencia = referencia,
                onReferenciaCambia = { referencia = it }
            )

            Spacer(Modifier.height(24.dp))

            ResumenPedido(subtotal = subtotal, delivery = delivery, total = total)

            Spacer(Modifier.height(24.dp))

            BotonPrimario(
                texto = "Confirmar pedido",
                // trim(): se envían los datos sin espacios sobrantes al inicio o al final
                onClick = {
                    onConfirmarPedido(nombre.trim(), telefono.trim(), direccion.trim(), referencia.trim())
                },
                habilitado = camposCompletos
            )

            // Mensaje de ayuda mientras el botón está desactivado, para que el
            // usuario sepa por qué no puede confirmar todavía
            if (!camposCompletos) {
                Text(
                    text = "Completa todos los campos para confirmar tu pedido",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

// Sub-composables PRIVADOS: solo los usa esta pantalla.

/**
 * Los 4 campos del formulario. No guarda estado propio: recibe cada valor
 * y avisa cada cambio hacia arriba, así DatosEntregaScreen sigue siendo
 * la única dueña de los textos.
 */
@Composable
private fun FormularioEntrega(
    nombre: String,
    onNombreCambia: (String) -> Unit,
    telefono: String,
    onTelefonoCambia: (String) -> Unit,
    direccion: String,
    onDireccionCambia: (String) -> Unit,
    referencia: String,
    onReferenciaCambia: (String) -> Unit
) {
    Column {
        CampoTexto(
            etiqueta = "Nombre completo",
            valor = nombre,
            onValorCambia = onNombreCambia,
            placeholder = "Juan Pérez"
        )
        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Teléfono",
            valor = telefono,
            onValorCambia = onTelefonoCambia,
            placeholder = "987 654 321",
            // Teclado numérico de teléfono en lugar del teclado de letras
            teclado = KeyboardType.Phone
        )
        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Dirección de entrega",
            valor = direccion,
            onValorCambia = onDireccionCambia,
            placeholder = "Av. Los Olivos 123"
        )
        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Referencia",
            valor = referencia,
            onValorCambia = onReferenciaCambia,
            placeholder = "Frente al parque"
        )
    }
}

/**
 * Recuadro gris con el resumen del pedido. Solo muestra los montos que
 * recibe; no calcula nada (los cálculos los hace ClienteApp).
 */
@Composable
private fun ResumenPedido(subtotal: Double, delivery: Double, total: Double) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(GrisClaro, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "Resumen del pedido",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        FilaResumen(etiqueta = "Subtotal", valor = subtotal)
        FilaResumen(etiqueta = "Costo de delivery", valor = delivery)

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        // El total va resaltado en verde, igual que en CarritoScreen
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Total", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "S/ %.2f".format(total),
                style = MaterialTheme.typography.titleMedium,
                color = VerdeBodega
            )
        }
    }
}

/** Una fila "etiqueta ........ S/ 0.00" del resumen. */
@Composable
private fun FilaResumen(etiqueta: String, valor: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        // SpaceBetween empuja la etiqueta a la izquierda y el monto a la derecha
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = etiqueta, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = "S/ %.2f".format(valor), color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DatosEntregaPreview() {
    BodegaTheme {
        // Montos de ejemplo: 2 Arroz Costeño (9.00) + 1 Coca-Cola (6.50) = 15.50
        DatosEntregaScreen(
            subtotal = 15.50,
            delivery = 4.00,
            total = 19.50,
            onVolver = {},
            onConfirmarPedido = { _, _, _, _ -> }
        )
    }
}
