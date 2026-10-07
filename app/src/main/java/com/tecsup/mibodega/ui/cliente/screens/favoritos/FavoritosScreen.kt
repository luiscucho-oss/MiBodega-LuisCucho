package com.tecsup.mibodega.ui.cliente.screens.favoritos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.componentes.ProductoCard
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.RojoPrecio

/**
 * Mis favoritos: los productos marcados con el corazón en Inicio, Categorías
 * o Detalle. Se abre con el corazón de la barra superior de Inicio.
 * No guarda estado propio: la lista llega ya filtrada desde ClienteApp y
 * cada toque se avisa hacia arriba (state hoisting).
 *
 * @param productos solo los productos favoritos
 * @param onQuitarFavorito se llama al tocar el corazón de una tarjeta: como
 *        todas están marcadas, tocarlo la quita de la lista
 */
@OptIn(ExperimentalMaterial3Api::class) // TopAppBar todavía es experimental en Material 3
@Composable
fun FavoritosScreen(
    productos: List<Producto>,
    onVolver: () -> Unit,
    onProductoClick: (Producto) -> Unit,
    onAgregarProducto: (Producto) -> Unit,
    onQuitarFavorito: (Producto) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis favoritos", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingInterno ->
        if (productos.isEmpty()) {
            SinFavoritos(modifier = Modifier.padding(paddingInterno))
        } else {
            // Mismo grid de 2 columnas y misma tarjeta que en Inicio
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingInterno)
            ) {
                // key = id: al quitar uno, Compose sabe cuál tarjeta desapareció
                items(productos, key = { it.id }) { producto ->
                    ProductoCard(
                        producto = producto,
                        esFavorito = true,
                        onClick = { onProductoClick(producto) },
                        onAgregar = { onAgregarProducto(producto) },
                        onFavoritoClick = { onQuitarFavorito(producto) }
                    )
                }
            }
        }
    }
}

// Sub-composables PRIVADOS: solo los usa esta pantalla.

/** Estado vacío: explica cómo agregar favoritos en vez de dejar la pantalla en blanco. */
@Composable
private fun SinFavoritos(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.FavoriteBorder,
            contentDescription = null, // decorativo: el texto de abajo ya lo dice
            tint = RojoPrecio,
            modifier = Modifier
                .size(96.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                .padding(24.dp)
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Aún no tienes favoritos",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "Toca el corazón de un producto para guardarlo aquí.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FavoritosPreview() {
    BodegaTheme {
        FavoritosScreen(
            productos = listaProductosFake.take(3),
            onVolver = {},
            onProductoClick = {},
            onAgregarProducto = {},
            onQuitarFavorito = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FavoritosVacioPreview() {
    BodegaTheme {
        FavoritosScreen(
            productos = emptyList(),
            onVolver = {},
            onProductoClick = {},
            onAgregarProducto = {},
            onQuitarFavorito = {}
        )
    }
}
