package com.tecsup.mibodega.ui.cliente.screens.pedidos

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.ItemCarrito
import com.tecsup.mibodega.ui.cliente.modelo.Pedido
import com.tecsup.mibodega.ui.cliente.modelo.TipoEntrega
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.componentes.BarraNavegacion
import com.tecsup.mibodega.ui.componentes.PestanaNavegacion
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.VerdeBodega

/**
 * Pestaña Pedidos: lista de los pedidos confirmados en esta sesión.
 * Solo muestra la lista que recibe; los pedidos se guardan en ClienteApp.
 *
 * @param pedidos en el orden en que se hicieron (el más antiguo primero)
 */
@OptIn(ExperimentalMaterial3Api::class) // TopAppBar todavía es experimental en Material 3
@Composable
fun PedidosScreen(
    pedidos: List<Pedido>,
    onNavegar: (PestanaNavegacion) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mis pedidos", fontWeight = FontWeight.Bold) })
        },
        bottomBar = {
            BarraNavegacion(
                pestanaActual = PestanaNavegacion.PEDIDOS,
                onPestanaClick = onNavegar
            )
        }
    ) { paddingInterno ->
        if (pedidos.isEmpty()) {
            // Estado vacío: un texto centrado en vez de una pantalla en blanco
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingInterno),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Aún no tienes pedidos",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingInterno)
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                // reversed(): el pedido más reciente aparece primero
                items(pedidos.reversed(), key = { it.numero }) { pedido ->
                    FilaPedido(pedido = pedido)
                    HorizontalDivider()
                }
            }
        }
    }
}

// Sub-composables PRIVADOS: solo los usa esta pantalla.

/** Número y total arriba; debajo, los productos y cómo se entrega. */
@Composable
private fun FilaPedido(pedido: Pedido) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Pedido #${pedido.numero}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "S/ %.2f".format(pedido.total),
                style = MaterialTheme.typography.titleMedium,
                color = VerdeBodega
            )
        }

        Spacer(Modifier.height(4.dp))

        // Ej.: "2 x Arroz Costeño, 1 x Coca-Cola Original"
        Text(
            text = pedido.productos.joinToString { "${it.cantidad} x ${it.producto.nombre}" },
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = when (pedido.tipoEntrega) {
                TipoEntrega.DELIVERY -> "Entrega en: ${pedido.direccion}"
                TipoEntrega.RECOJO_EN_TIENDA -> "Recojo en tienda"
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
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
                    productos = listOf(ItemCarrito(listaProductosFake[0], 2)),
                    total = 13.00,
                    tipoEntrega = TipoEntrega.DELIVERY,
                    direccion = "Av. Los Olivos 123"
                )
            ),
            onNavegar = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PedidosVacioPreview() {
    BodegaTheme {
        PedidosScreen(pedidos = emptyList(), onNavegar = {})
    }
}
