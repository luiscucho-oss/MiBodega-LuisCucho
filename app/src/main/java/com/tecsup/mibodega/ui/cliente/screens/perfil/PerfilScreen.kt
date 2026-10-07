package com.tecsup.mibodega.ui.cliente.screens.perfil

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.Usuario
import com.tecsup.mibodega.ui.cliente.modelo.usuarioDeEjemplo
import com.tecsup.mibodega.ui.componentes.BarraNavegacion
import com.tecsup.mibodega.ui.componentes.BotonSecundario
import com.tecsup.mibodega.ui.componentes.PestanaNavegacion
import com.tecsup.mibodega.ui.theme.BodegaTheme

/**
 * Pestaña Perfil: muestra los datos del usuario que entró a la app.
 * No guarda nada ni navega sola: los datos llegan desde ClienteApp y
 * "Cerrar sesión" solo avisa hacia arriba (onCerrarSesion).
 */
@OptIn(ExperimentalMaterial3Api::class) // TopAppBar todavía es experimental en Material 3
@Composable
fun PerfilScreen(
    usuario: Usuario,
    onCerrarSesion: () -> Unit,
    onNavegar: (PestanaNavegacion) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mi perfil", fontWeight = FontWeight.Bold) })
        },
        bottomBar = {
            BarraNavegacion(
                pestanaActual = PestanaNavegacion.PERFIL,
                onPestanaClick = onNavegar
            )
        }
    ) { paddingInterno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
                .padding(horizontal = 24.dp, vertical = 8.dp)
        ) {
            DatoPerfil(etiqueta = "Nombre", valor = usuario.nombre)
            DatoPerfil(etiqueta = "Teléfono", valor = usuario.telefono)
            DatoPerfil(etiqueta = "Dirección", valor = usuario.direccion)
            DatoPerfil(etiqueta = "Referencia", valor = usuario.referencia)

            // weight(1f) ocupa todo el espacio libre y empuja el botón hacia abajo
            Spacer(Modifier.weight(1f))

            BotonSecundario(
                texto = "Cerrar sesión",
                onClick = onCerrarSesion
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}

// Sub-composables PRIVADOS: solo los usa esta pantalla.

/** Una fila "Etiqueta / valor" con una línea divisoria debajo. */
@Composable
private fun DatoPerfil(etiqueta: String, valor: String) {
    Column(modifier = Modifier.padding(vertical = 10.dp)) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyLarge
        )
    }
    HorizontalDivider()
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PerfilPreview() {
    BodegaTheme {
        PerfilScreen(usuario = usuarioDeEjemplo, onCerrarSesion = {}, onNavegar = {})
    }
}
