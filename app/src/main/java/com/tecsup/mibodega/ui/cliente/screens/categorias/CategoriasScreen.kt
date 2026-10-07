package com.tecsup.mibodega.ui.cliente.screens.categorias

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.LocalDrink
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaCategorias
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.componentes.BarraNavegacion
import com.tecsup.mibodega.ui.componentes.PestanaNavegacion
import com.tecsup.mibodega.ui.componentes.ProductoCard
import com.tecsup.mibodega.ui.theme.AzulEnlace
import com.tecsup.mibodega.ui.theme.AzulTexto
import com.tecsup.mibodega.ui.theme.Blanco
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.GrisTexto
import com.tecsup.mibodega.ui.theme.RojoPrecio
import com.tecsup.mibodega.ui.theme.VerdeBodega

/**
 * Pestaña Categorías: una sección por cada categoría (Bebidas, Abarrotes,
 * Snacks) con un encabezado (ícono, color propio y contador de productos)
 * y una fila horizontal (LazyRow) de productos.
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
        // Fondo gris muy claro: las tarjetas blancas con sombra resaltan encima
        containerColor = GrisClaro,
        topBar = {
            TopAppBar(
                title = { Text("Categorías", fontWeight = FontWeight.Bold, color = AzulTexto) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GrisClaro),
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
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito", tint = AzulTexto)
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
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
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

/**
 * Ícono y color propios de cada categoría. Así cada sección se reconoce
 * de un vistazo y las tarjetas de esa categoría usan el mismo color.
 */
private data class EstiloCategoria(val icono: ImageVector, val color: Color)

private fun estiloDe(categoria: String): EstiloCategoria = when (categoria) {
    "Bebidas" -> EstiloCategoria(Icons.Default.LocalDrink, AzulEnlace)
    "Abarrotes" -> EstiloCategoria(Icons.Default.Kitchen, VerdeBodega)
    "Snacks" -> EstiloCategoria(Icons.Default.Fastfood, RojoPrecio)
    // Una categoría nueva que todavía no tenga estilo usa uno neutro
    else -> EstiloCategoria(Icons.Default.Category, GrisTexto)
}

/** Encabezado de la categoría + fila horizontal con sus productos. */
@Composable
private fun SeccionCategoria(
    nombre: String,
    productos: List<Producto>,
    onProductoClick: (Producto) -> Unit,
    onAgregarProducto: (Producto) -> Unit
) {
    val estilo = estiloDe(nombre)

    Column {
        EncabezadoCategoria(nombre = nombre, cantidad = productos.size, estilo = estilo)

        Spacer(Modifier.height(4.dp))

        LazyRow(
            // contentPadding (y no padding) para que las tarjetas puedan
            // deslizarse hasta el borde de la pantalla al hacer scroll.
            // El espacio vertical evita que se corte la sombra de las tarjetas.
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // key = id: Compose reconoce cada tarjeta aunque la lista cambie
            items(productos, key = { it.id }) { producto ->
                ProductoCard(
                    producto = producto,
                    onClick = { onProductoClick(producto) },
                    onAgregar = { onAgregarProducto(producto) },
                    // En una fila horizontal la tarjeta necesita un ancho fijo
                    // (en el grid de Inicio el ancho lo da la columna)
                    modifier = Modifier.width(160.dp),
                    // Cada tarjeta toma el color de su categoría
                    colorAcento = estilo.color
                )
            }
        }
    }
}

/**
 * Ícono en un círculo de color suave, nombre de la categoría,
 * contador de productos y una "píldora" con el número a la derecha.
 */
@Composable
private fun EncabezadoCategoria(nombre: String, cantidad: Int, estilo: EstiloCategoria) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(estilo.color.copy(alpha = 0.15f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = estilo.icono,
                contentDescription = null, // decorativo: el nombre va al lado
                tint = estilo.color,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = nombre,
                style = MaterialTheme.typography.titleMedium,
                color = AzulTexto
            )
            // Singular o plural según la cantidad: "1 producto" / "3 productos"
            Text(
                text = if (cantidad == 1) "1 producto" else "$cantidad productos",
                style = MaterialTheme.typography.bodySmall,
                color = GrisTexto
            )
        }

        // Píldora con el número, en el color de la categoría
        Text(
            text = "$cantidad",
            style = MaterialTheme.typography.labelMedium,
            color = Blanco,
            modifier = Modifier
                .background(estilo.color, RoundedCornerShape(50))
                .padding(horizontal = 10.dp, vertical = 2.dp)
        )
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
