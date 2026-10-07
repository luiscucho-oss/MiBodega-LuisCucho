package com.tecsup.mibodega.ui.cliente.screens.categorias

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaCategorias
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.componentes.BarraNavegacion
import com.tecsup.mibodega.ui.componentes.PestanaNavegacion
import com.tecsup.mibodega.ui.componentes.ProductoCard
import com.tecsup.mibodega.ui.theme.BodegaTheme

/**
 * Pestaña Categorías: una sección por cada categoría (Bebidas, Abarrotes,
 * Snacks) con su título y una fila horizontal (LazyRow) de productos.
 * No toca el carrito ni navega sola: tocar un producto o su "+" solo
 * avisa hacia arriba, igual que en InicioScreen (state hoisting).
 *
 * @param cantidadCarrito para el badge del carrito en la topBar
 */
@OptIn(ExperimentalMaterial3Api::class) // TopAppBar todavía es experimental en Material 3
@Composable
fun CategoriasScreen(
    productos: List<Producto> = listaProductosFake,
    cantidadCarrito: Int,
    onVerCarrito: () -> Unit,
    onProductoClick: (Producto) -> Unit,
    onAgregarProducto: (Producto) -> Unit,
    onNavegar: (PestanaNavegacion) -> Unit
) {
    // "Todos" es solo un filtro de Inicio, no una categoría real: se quita
    val categorias = listaCategorias.filter { it != "Todos" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Categorías", fontWeight = FontWeight.Bold) },
                actions = {
                    // Mismo acceso al carrito que en Inicio, para ver lo que se
                    // va agregando sin tener que cambiar de pestaña
                    IconButton(onClick = onVerCarrito) {
                        BadgedBox(
                            badge = {
                                if (cantidadCarrito > 0) {
                                    Badge { Text("$cantidadCarrito") }
                                }
                            }
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                        }
                    }
                }
            )
        },
        bottomBar = {
            BarraNavegacion(
                pestanaActual = PestanaNavegacion.CATEGORIAS,
                onPestanaClick = onNavegar
            )
        }
    ) { paddingInterno ->
        // LazyColumn vertical con una sección por categoría; dentro de cada
        // sección, un LazyRow horizontal con sus productos
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(categorias) { categoria ->
                SeccionCategoria(
                    nombre = categoria,
                    productos = productos.filter { it.categoria == categoria },
                    onProductoClick = onProductoClick,
                    onAgregarProducto = onAgregarProducto
                )
            }
        }
    }
}

// Sub-composables PRIVADOS: solo los usa esta pantalla.

/** Título de la categoría + fila horizontal con sus productos. */
@Composable
private fun SeccionCategoria(
    nombre: String,
    productos: List<Producto>,
    onProductoClick: (Producto) -> Unit,
    onAgregarProducto: (Producto) -> Unit
) {
    Column {
        Text(
            text = nombre,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(8.dp))

        LazyRow(
            // contentPadding (y no padding) para que las tarjetas puedan
            // deslizarse hasta el borde de la pantalla al hacer scroll
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // key = id: Compose reconoce cada tarjeta aunque la lista cambie
            items(productos, key = { it.id }) { producto ->
                ProductoCard(
                    producto = producto,
                    onClick = { onProductoClick(producto) },
                    onAgregar = { onAgregarProducto(producto) },
                    // En una fila horizontal la tarjeta necesita un ancho fijo
                    // (en el grid de Inicio el ancho lo da la columna)
                    modifier = Modifier.width(150.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CategoriasPreview() {
    BodegaTheme {
        CategoriasScreen(
            cantidadCarrito = 2,
            onVerCarrito = {},
            onProductoClick = {},
            onAgregarProducto = {},
            onNavegar = {}
        )
    }
}
