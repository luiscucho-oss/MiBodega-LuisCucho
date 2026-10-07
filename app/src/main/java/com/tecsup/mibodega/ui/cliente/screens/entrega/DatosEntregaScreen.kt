package com.tecsup.mibodega.ui.cliente.screens.entrega

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                .padding(horizontal = 24.dp)
        ) {
            // Aquí irán el formulario de entrega y el resumen del pedido
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DatosEntregaPreview() {
    BodegaTheme {
        DatosEntregaScreen(onVolver = {})
    }
}
