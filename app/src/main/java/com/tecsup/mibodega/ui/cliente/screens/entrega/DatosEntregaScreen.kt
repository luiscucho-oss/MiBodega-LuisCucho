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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.TipoEntrega
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
 * @param tipoEntrega opción marcada con los RadioButton (delivery o recojo)
 * @param total subtotal + tipoEntrega.costo (lo calcula ClienteApp)
 * @param onTipoEntregaCambia avisa qué opción se tocó; ClienteApp la guarda y
 *        vuelve a calcular el total
 * @param onConfirmarPedido recibe los datos ya validados (ningún campo vacío).
 *        Si falta algún campo no se llama: el campo vacío se marca en rojo.
 *        Con recojo en tienda, dirección y referencia llegan vacías.
 */
@OptIn(ExperimentalMaterial3Api::class) // TopAppBar todavía es experimental en Material 3
@Composable
fun DatosEntregaScreen(
    subtotal: Double,
    tipoEntrega: TipoEntrega,
    total: Double,
    onTipoEntregaCambia: (TipoEntrega) -> Unit,
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

    // Con recojo en tienda no hace falta dirección ni referencia: esos campos se
    // ocultan y solo se piden nombre y teléfono (para avisarle que ya está listo)
    val esDelivery = tipoEntrega == TipoEntrega.DELIVERY
    val camposObligatorios = if (esDelivery) {
        listOf(nombre, telefono, direccion, referencia)
    } else {
        listOf(nombre, telefono)
    }

    // El pedido solo se confirma si los campos obligatorios tienen texto. Se usa
    // isNotBlank() (y no isNotEmpty()) para que un campo con solo espacios cuente
    // como vacío. Como se calcula en cada recomposición, se actualiza solo al escribir.
    val camposCompletos = camposObligatorios.all { it.isNotBlank() }

    // Pasa a true la primera vez que se toca "Confirmar pedido". Desde ahí los
    // campos vacíos se ven en rojo hasta que el usuario escriba en ellos.
    var intentoConfirmar by remember { mutableStateOf(false) }

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
                text = "¿Cómo quieres recibir tu pedido?",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
            )

            SelectorTipoEntrega(
                seleccionado = tipoEntrega,
                onSeleccionar = onTipoEntregaCambia
            )

            Spacer(Modifier.height(20.dp))

            FormularioEntrega(
                pedirDireccion = esDelivery,
                nombre = nombre,
                onNombreCambia = { nombre = it },
                telefono = telefono,
                onTelefonoCambia = { telefono = it },
                direccion = direccion,
                onDireccionCambia = { direccion = it },
                referencia = referencia,
                onReferenciaCambia = { referencia = it },
                mostrarErrores = intentoConfirmar
            )

            Spacer(Modifier.height(24.dp))

            ResumenPedido(subtotal = subtotal, tipoEntrega = tipoEntrega, total = total)

            Spacer(Modifier.height(24.dp))

            BotonPrimario(
                texto = "Confirmar pedido",
                onClick = {
                    if (camposCompletos) {
                        // trim(): se envían los datos sin espacios sobrantes al inicio o al final.
                        // Con recojo, la dirección no se usa aunque se haya escrito antes.
                        onConfirmarPedido(
                            nombre.trim(),
                            telefono.trim(),
                            if (esDelivery) direccion.trim() else "",
                            if (esDelivery) referencia.trim() else ""
                        )
                    } else {
                        // No se avanza: solo se marcan en rojo los campos vacíos
                        intentoConfirmar = true
                    }
                }
            )

            // Mensaje junto al botón para que el usuario sepa por qué no avanzó,
            // aunque el campo vacío haya quedado más arriba (fuera de la pantalla)
            if (intentoConfirmar && !camposCompletos) {
                Text(
                    text = "Completa los campos marcados en rojo",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
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
 * Las dos opciones de entrega con RadioButton. Solo una puede estar marcada:
 * la que llega en "seleccionado". Al tocar otra, se avisa hacia arriba.
 */
@Composable
private fun SelectorTipoEntrega(
    seleccionado: TipoEntrega,
    onSeleccionar: (TipoEntrega) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(GrisClaro, RoundedCornerShape(12.dp))
            .padding(vertical = 4.dp)
            // selectableGroup: el lector de pantalla anuncia las opciones como un
            // solo grupo de RadioButton ("1 de 2", "2 de 2")
            .selectableGroup()
    ) {
        // entries = las opciones del enum, en el orden en que se declararon
        TipoEntrega.entries.forEach { tipo ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    // Toda la fila se puede tocar, no solo el circulito
                    .selectable(
                        selected = tipo == seleccionado,
                        onClick = { onSeleccionar(tipo) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = tipo == seleccionado,
                    // null: el toque ya lo maneja la fila completa (selectable)
                    onClick = null,
                    colors = RadioButtonDefaults.colors(selectedColor = VerdeBodega)
                )
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = tipo.etiqueta, style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = tipo.descripcion,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = if (tipo.costo == 0.0) "Gratis" else "S/ %.2f".format(tipo.costo),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = VerdeBodega
                )
            }
        }
    }
}

/**
 * Los campos del formulario. No guarda estado propio: recibe cada valor
 * y avisa cada cambio hacia arriba, así DatosEntregaScreen sigue siendo
 * la única dueña de los textos.
 *
 * @param pedirDireccion false con recojo en tienda: dirección y referencia
 *        no se muestran porque no hacen falta
 * @param mostrarErrores true después de tocar "Confirmar pedido": desde ahí
 *        cada campo vacío se marca en rojo
 */
@Composable
private fun FormularioEntrega(
    pedirDireccion: Boolean,
    nombre: String,
    onNombreCambia: (String) -> Unit,
    telefono: String,
    onTelefonoCambia: (String) -> Unit,
    direccion: String,
    onDireccionCambia: (String) -> Unit,
    referencia: String,
    onReferenciaCambia: (String) -> Unit,
    mostrarErrores: Boolean
) {
    Column {
        CampoTexto(
            etiqueta = "Nombre completo",
            valor = nombre,
            onValorCambia = onNombreCambia,
            esError = mostrarErrores && nombre.isBlank(),
            placeholder = "Juan Pérez"
        )
        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Teléfono",
            valor = telefono,
            onValorCambia = onTelefonoCambia,
            esError = mostrarErrores && telefono.isBlank(),
            placeholder = "987 654 321",
            // Teclado numérico de teléfono en lugar del teclado de letras
            teclado = KeyboardType.Phone
        )

        // Solo con delivery: para recoger en la bodega no se necesita dirección
        if (pedirDireccion) {
            Spacer(Modifier.height(16.dp))

            CampoTexto(
                etiqueta = "Dirección de entrega",
                valor = direccion,
                onValorCambia = onDireccionCambia,
                esError = mostrarErrores && direccion.isBlank(),
                placeholder = "Av. Los Olivos 123"
            )
            Spacer(Modifier.height(16.dp))

            CampoTexto(
                etiqueta = "Referencia",
                valor = referencia,
                onValorCambia = onReferenciaCambia,
                esError = mostrarErrores && referencia.isBlank(),
                placeholder = "Frente al parque"
            )
        }
    }
}

/**
 * Recuadro gris con el resumen del pedido. Solo muestra los montos que
 * recibe; no calcula nada (los cálculos los hace ClienteApp).
 */
@Composable
private fun ResumenPedido(subtotal: Double, tipoEntrega: TipoEntrega, total: Double) {
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
        // "Delivery S/ 4.00" o "Recojo en tienda S/ 0.00": por eso el total cambia
        // apenas se marca otra opción
        FilaResumen(etiqueta = tipoEntrega.etiqueta, valor = tipoEntrega.costo)

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
            tipoEntrega = TipoEntrega.DELIVERY,
            total = 19.50,
            onTipoEntregaCambia = {},
            onVolver = {},
            onConfirmarPedido = { _, _, _, _ -> }
        )
    }
}
