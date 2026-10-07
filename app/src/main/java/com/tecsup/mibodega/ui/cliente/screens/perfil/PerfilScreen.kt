package com.tecsup.mibodega.ui.cliente.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.mibodega.ui.cliente.modelo.Usuario
import com.tecsup.mibodega.ui.cliente.modelo.usuarioDeEjemplo
import com.tecsup.mibodega.ui.componentes.BarraNavegacion
import com.tecsup.mibodega.ui.componentes.BotonSecundario
import com.tecsup.mibodega.ui.componentes.PestanaNavegacion
import com.tecsup.mibodega.ui.theme.AzulEnlace
import com.tecsup.mibodega.ui.theme.AzulTexto
import com.tecsup.mibodega.ui.theme.Blanco
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.GrisTexto
import com.tecsup.mibodega.ui.theme.RojoPrecio
import com.tecsup.mibodega.ui.theme.VerdeBodega

/**
 * Pestaña Perfil: avatar con iniciales, resumen de compras y datos del usuario.
 * No guarda nada ni navega sola: los datos llegan desde ClienteApp y
 * "Cerrar sesión" solo avisa hacia arriba (onCerrarSesion) DESPUÉS de que
 * el usuario lo confirma en el diálogo.
 *
 * @param cantidadPedidos cuántos pedidos confirmó en esta sesión
 * @param totalGastado suma de los totales de esos pedidos
 */
@OptIn(ExperimentalMaterial3Api::class) // TopAppBar todavía es experimental en Material 3
@Composable
fun PerfilScreen(
    usuario: Usuario,
    cantidadPedidos: Int,
    totalGastado: Double,
    onCerrarSesion: () -> Unit,
    onNavegar: (PestanaNavegacion) -> Unit
) {
    // Estado visual local: si el diálogo de confirmación está abierto
    var mostrarConfirmacion by remember { mutableStateOf(false) }

    if (mostrarConfirmacion) {
        DialogoCerrarSesion(
            onConfirmar = {
                mostrarConfirmacion = false
                onCerrarSesion()
            },
            onCancelar = { mostrarConfirmacion = false }
        )
    }

    Scaffold(
        // Fondo gris muy claro: las tarjetas blancas con sombra resaltan encima
        containerColor = GrisClaro,
        topBar = {
            TopAppBar(
                title = { Text("Mi perfil", fontWeight = FontWeight.Bold, color = AzulTexto) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GrisClaro)
            )
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
                // verticalScroll: en pantallas pequeñas se puede bajar hasta el botón
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(8.dp))

            AvatarIniciales(nombre = usuario.nombre)

            Spacer(Modifier.height(12.dp))

            Text(
                text = usuario.nombre,
                style = MaterialTheme.typography.titleLarge,
                color = AzulTexto
            )
            Text(
                text = "Cliente de Mi Bodega",
                style = MaterialTheme.typography.bodyMedium,
                color = GrisTexto
            )

            Spacer(Modifier.height(20.dp))

            // Resumen de compras: dos tarjetas del mismo ancho (weight 1f cada una)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TarjetaResumen(
                    icono = Icons.Default.Receipt,
                    color = VerdeBodega,
                    valor = "$cantidadPedidos",
                    etiqueta = if (cantidadPedidos == 1) "pedido" else "pedidos",
                    modifier = Modifier.weight(1f)
                )
                TarjetaResumen(
                    icono = Icons.Default.Payments,
                    color = AzulEnlace,
                    valor = "S/ %.2f".format(totalGastado),
                    etiqueta = "total gastado",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(16.dp))

            TarjetaDatos(usuario = usuario)

            Spacer(Modifier.height(24.dp))

            // Solo abre el diálogo; cerrar sesión de verdad pasa al confirmar
            BotonSecundario(
                texto = "Cerrar sesión",
                onClick = { mostrarConfirmacion = true }
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}

// Sub-composables y funciones PRIVADAS: solo los usa esta pantalla.

/**
 * Iniciales para el avatar: primera letra de las dos primeras palabras.
 * "Luis Cucho" → "LC", "Ana" → "A". Si el nombre está vacío, "?".
 */
private fun inicialesDe(nombre: String): String =
    nombre.trim()
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }
        .ifEmpty { "?" }

/** Círculo verde con las iniciales en blanco. */
@Composable
private fun AvatarIniciales(nombre: String) {
    Box(
        modifier = Modifier
            .size(96.dp)
            .shadow(8.dp, CircleShape, spotColor = VerdeBodega.copy(alpha = 0.4f))
            .background(VerdeBodega, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = inicialesDe(nombre),
            color = Blanco,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/** Tarjeta pequeña con un ícono de color, un valor grande y su etiqueta. */
@Composable
private fun TarjetaResumen(
    icono: ImageVector,
    color: Color,
    valor: String,
    etiqueta: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Blanco),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(color.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icono, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.height(10.dp))
            Text(text = valor, style = MaterialTheme.typography.titleLarge, color = AzulTexto)
            Text(text = etiqueta, style = MaterialTheme.typography.bodySmall, color = GrisTexto)
        }
    }
}

/** Tarjeta blanca con los 4 datos del usuario, cada uno con su ícono. */
@Composable
private fun TarjetaDatos(usuario: Usuario) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Blanco),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text(
                text = "Mis datos",
                style = MaterialTheme.typography.titleMedium,
                color = AzulTexto,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            FilaDato(icono = Icons.Default.Person, etiqueta = "Nombre", valor = usuario.nombre)
            HorizontalDivider(color = GrisClaro)
            FilaDato(icono = Icons.Default.Phone, etiqueta = "Teléfono", valor = usuario.telefono)
            HorizontalDivider(color = GrisClaro)
            FilaDato(icono = Icons.Default.Home, etiqueta = "Dirección", valor = usuario.direccion)
            HorizontalDivider(color = GrisClaro)
            FilaDato(icono = Icons.Default.Place, etiqueta = "Referencia", valor = usuario.referencia)
        }
    }
}

/** Ícono gris + etiqueta pequeña arriba + valor abajo. */
@Composable
private fun FilaDato(icono: ImageVector, etiqueta: String, valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icono, contentDescription = null, tint = GrisTexto, modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(14.dp))
        Column {
            Text(text = etiqueta, style = MaterialTheme.typography.bodySmall, color = GrisTexto)
            Text(text = valor, style = MaterialTheme.typography.bodyLarge, color = AzulTexto)
        }
    }
}

/**
 * Diálogo de confirmación: evita cerrar sesión por un toque accidental.
 * "Sí, cerrar sesión" va en rojo porque es una acción que no se deshace.
 */
@Composable
private fun DialogoCerrarSesion(onConfirmar: () -> Unit, onCancelar: () -> Unit) {
    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("¿Cerrar sesión?") },
        text = { Text("Se vaciará tu carrito y volverás a la pantalla de bienvenida.") },
        confirmButton = {
            TextButton(onClick = onConfirmar) {
                Text("Sí, cerrar sesión", color = RojoPrecio)
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) {
                Text("Cancelar", color = GrisTexto)
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PerfilPreview() {
    BodegaTheme {
        PerfilScreen(
            usuario = usuarioDeEjemplo,
            cantidadPedidos = 2,
            totalGastado = 32.50,
            onCerrarSesion = {},
            onNavegar = {}
        )
    }
}
