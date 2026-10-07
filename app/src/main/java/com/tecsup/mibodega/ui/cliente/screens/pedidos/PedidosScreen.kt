package com.tecsup.mibodega.ui.cliente.screens.pedidos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.ItemCarrito
import com.tecsup.mibodega.ui.cliente.modelo.Pedido
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.componentes.BarraNavegacion
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.PestanaNavegacion
import com.tecsup.mibodega.ui.theme.AzulTexto
import com.tecsup.mibodega.ui.theme.Blanco
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.GrisTexto
import com.tecsup.mibodega.ui.theme.VerdeBodega
import com.tecsup.mibodega.ui.theme.VerdeOscuro
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Pestaña Pedidos: una tarjeta por cada pedido confirmado en esta sesión.
 * Solo muestra la lista que recibe; los pedidos se guardan en ClienteApp.
 *
 * @param pedidos en el orden en que se hicieron (el más antiguo primero)
 * @param onIrAComprar botón del estado vacío: ClienteApp lleva a Inicio
 */
@OptIn(ExperimentalMaterial3Api::class) // TopAppBar todavía es experimental en Material 3
@Composable
fun PedidosScreen(
    pedidos: List<Pedido>,
    onIrAComprar: () -> Unit,
    onNavegar: (PestanaNavegacion) -> Unit
) {
    Scaffold(
        // Fondo gris muy claro: las tarjetas blancas con sombra resaltan encima
        containerColor = GrisClaro,
        topBar = {
            TopAppBar(
                title = { Text("Mis pedidos", fontWeight = FontWeight.Bold, color = AzulTexto) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GrisClaro)
            )
        },
        bottomBar = {
            BarraNavegacion(
                pestanaActual = PestanaNavegacion.PEDIDOS,
                onPestanaClick = onNavegar
            )
        }
    ) { paddingInterno ->
        if (pedidos.isEmpty()) {
            EstadoSinPedidos(
                onIrAComprar = onIrAComprar,
                modifier = Modifier.padding(paddingInterno)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingInterno),
                // contentPadding (y no padding) para que la sombra de las
                // tarjetas no se corte en los bordes
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // reversed(): el pedido más reciente aparece primero
                items(pedidos.reversed(), key = { it.numero }) { pedido ->
                    TarjetaPedido(pedido = pedido)
                }
            }
        }
    }
}

// Sub-composables y funciones PRIVADAS: solo los usa esta pantalla.

// Formato de fecha en español de Perú: "07/10/2026 · 03:15 p. m."
private val FORMATO_FECHA = SimpleDateFormat("dd/MM/yyyy · hh:mm a", Locale.forLanguageTag("es-PE"))

// Cuántos productos se listan en la tarjeta antes de resumir con "+N más"
private const val MAXIMO_PRODUCTOS_VISIBLES = 3

/**
 * Tarjeta de un pedido:
 * - Arriba: ícono, número, fecha y chip de estado.
 * - Al medio: hasta 3 productos con su subtotal (y "+N más" si hay más).
 * - Abajo: dirección de entrega y el total destacado en verde.
 */
@Composable
private fun TarjetaPedido(pedido: Pedido) {
    val forma = RoundedCornerShape(20.dp)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = forma,
                ambientColor = AzulTexto.copy(alpha = 0.08f),
                spotColor = AzulTexto.copy(alpha = 0.16f)
            ),
        shape = forma,
        colors = CardDefaults.cardColors(containerColor = Blanco)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // --- Encabezado ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(VerdeBodega.copy(alpha = 0.12f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Receipt,
                        contentDescription = null,
                        tint = VerdeBodega
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Pedido #${pedido.numero}",
                        style = MaterialTheme.typography.titleMedium,
                        color = AzulTexto
                    )
                    Text(
                        text = FORMATO_FECHA.format(Date(pedido.fecha)),
                        style = MaterialTheme.typography.bodySmall,
                        color = GrisTexto
                    )
                }
                ChipEstado(texto = "En camino")
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = GrisClaro)

            // --- Productos (resumidos) ---
            pedido.productos.take(MAXIMO_PRODUCTOS_VISIBLES).forEach { item ->
                FilaProducto(item = item)
            }
            val restantes = pedido.productos.size - MAXIMO_PRODUCTOS_VISIBLES
            if (restantes > 0) {
                Text(
                    text = if (restantes == 1) "+1 producto más" else "+$restantes productos más",
                    style = MaterialTheme.typography.bodySmall,
                    color = GrisTexto,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = GrisClaro)

            // --- Dirección y total ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    tint = GrisTexto,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = pedido.direccion,
                    style = MaterialTheme.typography.bodySmall,
                    color = GrisTexto,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Total", style = MaterialTheme.typography.titleMedium, color = AzulTexto)
                Text(
                    text = "S/ %.2f".format(pedido.total),
                    style = MaterialTheme.typography.titleLarge,
                    color = VerdeBodega
                )
            }
        }
    }
}

/** "2 × Arroz Costeño ........ S/ 9.00" */
@Composable
private fun FilaProducto(item: ItemCarrito) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Text(
            text = "${item.cantidad} × ${item.producto.nombre}",
            style = MaterialTheme.typography.bodyMedium,
            color = AzulTexto,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "S/ %.2f".format(item.producto.precio * item.cantidad),
            style = MaterialTheme.typography.bodyMedium,
            color = GrisTexto
        )
    }
}

/** Píldora verde con un camioncito y el estado del pedido. */
@Composable
private fun ChipEstado(texto: String) {
    Row(
        modifier = Modifier
            .background(VerdeBodega.copy(alpha = 0.12f), RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.LocalShipping,
            contentDescription = null,
            tint = VerdeOscuro,
            modifier = Modifier.size(14.dp)
        )
        Spacer(Modifier.width(4.dp))
        Text(text = texto, style = MaterialTheme.typography.labelMedium, color = VerdeOscuro)
    }
}

/**
 * Estado vacío: ícono grande en un círculo blanco, mensaje y un botón
 * "Ir a comprar" (avisa hacia arriba; ClienteApp lleva a Inicio).
 */
@Composable
private fun EstadoSinPedidos(onIrAComprar: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Blanco, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingBag,
                contentDescription = null,
                tint = VerdeBodega,
                modifier = Modifier.size(60.dp)
            )
        }
        Spacer(Modifier.height(20.dp))
        Text(
            text = "Aún no tienes pedidos",
            style = MaterialTheme.typography.titleMedium,
            color = AzulTexto
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Cuando confirmes una compra, la verás aquí con su estado.",
            style = MaterialTheme.typography.bodyMedium,
            color = GrisTexto,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        BotonPrimario(texto = "Ir a comprar", onClick = onIrAComprar)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PedidosPreview() {
    BodegaTheme {
        PedidosScreen(
            pedidos = listOf(
                Pedido(
                    numero = 1,
                    productos = listOf(
                        ItemCarrito(listaProductosFake[0], 2),
                        ItemCarrito(listaProductosFake[4], 1)
                    ),
                    total = 19.50,
                    direccion = "Av. Los Olivos 123"
                )
            ),
            onIrAComprar = {},
            onNavegar = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PedidosVacioPreview() {
    BodegaTheme {
        PedidosScreen(pedidos = emptyList(), onIrAComprar = {}, onNavegar = {})
    }
}
