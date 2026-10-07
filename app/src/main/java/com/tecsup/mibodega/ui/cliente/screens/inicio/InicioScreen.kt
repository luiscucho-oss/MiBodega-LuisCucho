package com.tecsup.mibodega.ui.cliente.screens.inicio

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaCategorias
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.componentes.ProductoCard
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.VerdeBodega
import java.text.Normalizer

/**
 * Pantalla 3: Inicio / Productos (mockup "Cliente").
 * La más completa: Scaffold (topBar + bottomBar), LazyRow de categorías
 * y LazyVerticalGrid de productos.
 *
 * @param productos lista completa (fake por ahora, luego vendrá de un ViewModel)
 * @param cantidadCarrito para el badge del carrito en la topBar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    productos: List<Producto> = listaProductosFake,
    cantidadCarrito: Int,
    onVerCarrito: () -> Unit,
    onProductoClick: (Producto) -> Unit,
    onAgregarProducto: (Producto) -> Unit
) {
    var categoriaSeleccionada by remember { mutableStateOf(listaCategorias.first()) }
    var textoBusqueda by remember { mutableStateOf("") }

    // Se normaliza UNA sola vez lo que escribió el usuario (sin tildes, en
    // minúsculas y sin espacios a los costados) para no repetirlo por cada producto
    val busquedaNormalizada = normalizarTexto(textoBusqueda)

    // Los dos filtros se combinan con "&&": un producto se muestra solo si
    // pasa el filtro de categoría Y también el de búsqueda
    val productosFiltrados = productos.filter { producto ->
        val coincideCategoria = categoriaSeleccionada == "Todos" || producto.categoria == categoriaSeleccionada
        // El nombre también se normaliza: así "cafe" encuentra "Café" y "COSTENO" encuentra "Costeño".
        // Si la búsqueda está vacía, contains("") es true y no se filtra nada.
        val coincideNombre = normalizarTexto(producto.nombre).contains(busquedaNormalizada)
        // También se busca en la descripción, con la misma normalización:
        // así "chocolate" encuentra "Galleta Oreo" aunque no esté en su nombre
        val coincideDescripcion = normalizarTexto(producto.descripcion).contains(busquedaNormalizada)
        // Basta con que coincida UNO de los dos (||)
        val coincideBusqueda = coincideNombre || coincideDescripcion
        coincideCategoria && coincideBusqueda
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Bodega", fontWeight = FontWeight.Bold) },
                actions = {
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
        bottomBar = { BarraInferior() }
    ) { paddingInterno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                placeholder = { Text("Buscar productos...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = GrisClaro,
                    focusedContainerColor = GrisClaro,
                    unfocusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                    focusedBorderColor = VerdeBodega
                )
            )

            Text(
                text = "Productos destacados",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 20.dp, bottom = 4.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(listaCategorias) { categoria ->
                    ChipCategoria(
                        texto = categoria,
                        seleccionado = categoria == categoriaSeleccionada,
                        onClick = { categoriaSeleccionada = categoria }
                    )
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(productosFiltrados) { producto ->
                    ProductoCard(
                        producto = producto,
                        onClick = { onProductoClick(producto) },
                        onAgregar = { onAgregarProducto(producto) }
                    )
                }
            }
        }
    }
}

// Funciones de apoyo para el buscador.

// Expresión regular para las "marcas" que acompañan a una letra (tilde, diéresis,
// virgulilla de la ñ...). \p{Mn} = "Mark, nonspacing" en Unicode.
// Se crea una sola vez porque compilar un Regex en cada búsqueda es costoso.
private val MARCAS_DE_ACENTO = Regex("\\p{Mn}+")

/**
 * Deja un texto listo para comparar en el buscador:
 * 1. Normalizer NFD separa cada letra de su tilde: "é" pasa a ser "e" + "´".
 * 2. Se borran esas tildes sueltas con MARCAS_DE_ACENTO: "Café" → "Cafe".
 * 3. lowercase() pasa todo a minúsculas: "Cafe" → "cafe".
 * 4. trim() quita los espacios del inicio y del final.
 * Ejemplo: normalizarTexto("  Arroz COSTEÑO ") devuelve "arroz costeno".
 */
private fun normalizarTexto(texto: String): String =
    Normalizer.normalize(texto, Normalizer.Form.NFD)
        .replace(MARCAS_DE_ACENTO, "")
        .lowercase()
        .trim()

// Sub-composables PRIVADOS: solo los usa esta pantalla.

@Composable
private fun ChipCategoria(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    val fondo = if (seleccionado) VerdeBodega else GrisClaro
    val contenido = if (seleccionado) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    Row(
        modifier = Modifier
            .background(fondo, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(text = texto, color = contenido, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun BarraInferior() {
    var seleccionado by remember { mutableStateOf(0) }
    val items = listOf(
        Triple("Inicio", Icons.Default.Home, 0),
        Triple("Categorías", Icons.Default.List, 1),
        Triple("Pedidos", Icons.Default.Receipt, 2),
        Triple("Perfil", Icons.Default.Person, 3)
    )
    NavigationBar {
        items.forEach { (etiqueta, icono, indice) ->
            NavigationBarItem(
                selected = seleccionado == indice,
                onClick = { seleccionado = indice },
                icon = { Icon(icono, contentDescription = etiqueta) },
                label = { Text(etiqueta) },
                colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                    selectedIconColor = VerdeBodega,
                    selectedTextColor = VerdeBodega
                )
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun InicioPreview() {
    BodegaTheme {
        InicioScreen(
            cantidadCarrito = 3,
            onVerCarrito = {},
            onProductoClick = {},
            onAgregarProducto = {}
        )
    }
}