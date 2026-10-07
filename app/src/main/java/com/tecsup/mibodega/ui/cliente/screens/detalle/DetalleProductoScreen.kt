package com.tecsup.mibodega.ui.cliente.screens.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.SelectorCantidad
import com.tecsup.mibodega.ui.theme.AzulTexto
import com.tecsup.mibodega.ui.theme.Blanco
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisTexto
import com.tecsup.mibodega.ui.theme.RojoPrecio
import com.tecsup.mibodega.ui.theme.VerdeBodega
import com.tecsup.mibodega.ui.theme.VerdeOscuro

/**
 * Pantalla 4: Detalle del producto (mockup "Cliente").
 * Guarda su propia cantidad seleccionada (remember) mientras el usuario
 * decide cuánto quiere; solo al tocar "Agregar al carrito" le avisa
 * a ClienteApp cuánto agregar.
 *
 * Diseño: imagen a todo el ancho con los botones flotando encima y una
 * tarjeta blanca de esquinas redondeadas que "sube" sobre la imagen.
 */
@Composable
fun DetalleProductoScreen(
    producto: Producto,
    onVolver: () -> Unit,
    onAgregarAlCarrito: (Producto, Int) -> Unit
) {
    var cantidad by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Blanco)
    ) {
        // Box: la imagen abajo y el encabezado (volver / favorito) encima
        Box {
            ImagenProducto()
            EncabezadoDetalle(
                onVolver = onVolver,
                // statusBarsPadding: los botones no quedan debajo de la barra de estado
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }

        // Tarjeta de información. offset(y = -28dp) la sube sobre la imagen y
        // las esquinas de arriba redondeadas hacen el efecto de "hoja".
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .offset(y = (-28).dp)
                .background(Blanco, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .navigationBarsPadding()
                .padding(start = 24.dp, end = 24.dp, top = 24.dp)
        ) {
            ChipCategoria(texto = producto.categoria)

            Spacer(Modifier.height(10.dp))

            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.titleLarge,
                color = AzulTexto
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "S/ %.2f".format(producto.precio),
                style = MaterialTheme.typography.displayMedium.copy(fontSize = 28.sp),
                color = RojoPrecio
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = producto.descripcion,
                style = MaterialTheme.typography.bodyLarge,
                color = GrisTexto
            )

            Spacer(Modifier.height(24.dp))

            // "Cantidad" a la izquierda y el selector a la derecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cantidad",
                    style = MaterialTheme.typography.titleMedium,
                    color = AzulTexto
                )
                SelectorCantidad(
                    cantidad = cantidad,
                    onIncrementar = { cantidad++ },
                    onDecrementar = { if (cantidad > 1) cantidad-- }
                )
            }

            // weight(1f) empuja el botón hasta abajo
            Spacer(Modifier.weight(1f))

            // El subtexto muestra el total según la cantidad elegida
            BotonPrimario(
                texto = "Agregar al carrito",
                subtexto = "Total: S/ %.2f".format(producto.precio * cantidad),
                onClick = { onAgregarAlCarrito(producto, cantidad) }
            )

            // Como la tarjeta subió 28dp con offset, abajo ya quedan 28dp libres;
            // este espacio extra separa un poco más el botón del borde
            Spacer(Modifier.height(12.dp))
        }
    }
}

// Sub-composables PRIVADOS: solo los usa esta pantalla.

@Composable
private fun EncabezadoDetalle(onVolver: () -> Unit, modifier: Modifier = Modifier) {
    // Estado LOCAL del favorito: solo este encabezado lo usa, así que vive aquí
    // con remember (igual que la cantidad en DetalleProductoScreen). Por ahora
    // no se guarda en ningún lado: al salir del detalle vuelve a false.
    var esFavorito by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onVolver) {
            FondoCircular {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = AzulTexto)
            }
        }
        // Cada toque invierte el valor (true ↔ false); al cambiar el estado,
        // Compose vuelve a dibujar el ícono con la nueva versión
        IconButton(onClick = { esFavorito = !esFavorito }) {
            FondoCircular {
                Icon(
                    // Corazón lleno si es favorito, corazón vacío (solo borde) si no
                    imageVector = if (esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    // La descripción cambia para que el lector de pantalla diga qué hará el toque
                    contentDescription = if (esFavorito) "Quitar de favoritos" else "Agregar a favoritos",
                    // Rojo cuando está marcado; color del texto cuando no
                    tint = if (esFavorito) RojoPrecio else AzulTexto
                )
            }
        }
    }
}

/**
 * Círculo blanco con sombra detrás de un ícono: así los botones se ven
 * bien encima de la imagen, sea del color que sea.
 */
@Composable
private fun FondoCircular(contenido: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .shadow(4.dp, CircleShape)
            .background(Blanco, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        contenido()
    }
}

@Composable
private fun ImagenProducto() {
    // Placeholder de imagen: reemplázalo por Image(painterResource(...))
    // cuando tengan la foto real de cada producto.
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.1f)
            .background(VerdeBodega.copy(alpha = 0.10f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingBasket,
            contentDescription = null,
            tint = VerdeBodega,
            modifier = Modifier.size(110.dp)
        )
    }
}

/** Píldora verde suave con el nombre de la categoría. */
@Composable
private fun ChipCategoria(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.labelMedium,
        color = VerdeOscuro,
        modifier = Modifier
            .background(VerdeBodega.copy(alpha = 0.12f), RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DetalleProductoPreview() {
    BodegaTheme {
        DetalleProductoScreen(
            producto = listaProductosFake.first { it.nombre == "Coca-Cola Original" },
            onVolver = {},
            onAgregarAlCarrito = { _, _ -> }
        )
    }
}
