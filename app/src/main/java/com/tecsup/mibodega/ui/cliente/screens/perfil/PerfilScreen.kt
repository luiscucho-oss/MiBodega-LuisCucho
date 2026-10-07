package com.tecsup.mibodega.ui.cliente.screens.perfil

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
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
 * Pestaña Perfil: muestra los datos del usuario que entró a la app y el
 * Switch de modo oscuro.
 * No guarda nada ni navega sola: los datos llegan desde ClienteApp y
 * "Cerrar sesión" y el Switch solo avisan hacia arriba.
 *
 * @param modoOscuro si el Switch está encendido (el estado vive en MainActivity)
 * @param onModoOscuroCambia avisa el nuevo valor al tocar el Switch
 */
@OptIn(ExperimentalMaterial3Api::class) // TopAppBar todavía es experimental en Material 3
@Composable
fun PerfilScreen(
    usuario: Usuario,
    modoOscuro: Boolean,
    onModoOscuroCambia: (Boolean) -> Unit,
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

            Spacer(Modifier.height(8.dp))

            FilaModoOscuro(activado = modoOscuro, onCambia = onModoOscuroCambia)

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

/** "Modo oscuro" con su Switch a la derecha. */
@Composable
private fun FilaModoOscuro(activado: Boolean, onCambia: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            // toggleable: toda la fila prende o apaga el Switch, no solo el interruptor
            .toggleable(value = activado, onValueChange = onCambia, role = Role.Switch)
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.DarkMode,
            contentDescription = null, // decorativo: el texto ya dice qué es
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = "Modo oscuro",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = activado,
            // null: el toque ya lo maneja la fila completa (toggleable)
            onCheckedChange = null
        )
    }
    HorizontalDivider()
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PerfilPreview() {
    BodegaTheme {
        PerfilScreen(
            usuario = usuarioDeEjemplo,
            modoOscuro = false,
            onModoOscuroCambia = {},
            onCerrarSesion = {},
            onNavegar = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PerfilOscuroPreview() {
    BodegaTheme(modoOscuro = true) {
        PerfilScreen(
            usuario = usuarioDeEjemplo,
            modoOscuro = true,
            onModoOscuroCambia = {},
            onCerrarSesion = {},
            onNavegar = {}
        )
    }
}
