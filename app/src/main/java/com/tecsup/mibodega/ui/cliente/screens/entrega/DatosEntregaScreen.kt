package com.tecsup.mibodega.ui.cliente.screens.entrega

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.CampoTexto
import com.tecsup.mibodega.ui.theme.BodegaTheme

/**
 * Pantalla 6: Datos de entrega (mockup "Cliente").
 * Se abre desde el carrito con "Continuar pedido".
 * No navega sola: el botón de volver solo avisa hacia arriba (onVolver)
 * y es ClienteApp quien decide qué hacer (state hoisting).
 */
@OptIn(ExperimentalMaterial3Api::class) // TopAppBar todavía es experimental en Material 3
@Composable
fun DatosEntregaScreen(
    onVolver: () -> Unit
) {
    // Estado del formulario: vive aquí con remember (igual que en RegistroScreen)
    // porque solo esta pantalla necesita los textos mientras el usuario escribe.
    // "by" permite leer y escribir la variable como si fuera un String normal.
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var referencia by remember { mutableStateOf("") }

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

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DatosEntregaPreview() {
    BodegaTheme {
        DatosEntregaScreen(onVolver = {})
    }
}
