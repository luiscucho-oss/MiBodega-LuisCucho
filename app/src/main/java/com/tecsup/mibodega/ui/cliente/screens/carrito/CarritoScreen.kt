package com.tecsup.mibodega.ui.cliente.screens.carrito

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RemoveShoppingCart
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.ItemCarrito
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.BotonSecundario
import com.tecsup.mibodega.ui.componentes.SelectorCantidad
import com.tecsup.mibodega.ui.theme.AzulTexto
import com.tecsup.mibodega.ui.theme.Blanco
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.GrisTexto
import com.tecsup.mibodega.ui.theme.VerdeBodega

// Público (sin "private") para que ClienteApp use el mismo valor al armar
// el resumen de DatosEntregaScreen: así el costo se cambia en un solo lugar.
const val COSTO_DELIVERY = 4.00

/**
 * Pantalla 5: Mi carrito (mockup "Cliente").
 * No guarda estado propio: el carrito viene de ClienteApp y cualquier
 * cambio (sumar, restar, eliminar) se avisa hacia arriba con callbacks.
 * Si el carrito está vacío, muestra un estado vacío y desactiva "Continuar pedido".
 *
 * Diseño: fondo gris claro, cada producto en una tarjeta blanca redondeada
 * y el resumen en un panel blanco inferior con las esquinas de arriba redondeadas.
 *
 * @param onIrAInicio botón "Volver al inicio" del estado vacío
 */
@Composable
fun CarritoScreen(
    carrito: List<ItemCarrito>,
    onVolver: () -> Unit,
    onIncrementar: (Producto) -> Unit,
    onDecrementar: (Producto) -> Unit,
    onEliminar: (Producto) -> Unit,
    onContinuarPedido: () -> Unit,
    onIrAInicio: () -> Unit
) {
    val subtotal = carrito.sumOf { it.producto.precio * it.cantidad }
    val total = subtotal + COSTO_DELIVERY

    Column(
        modifier = Modifier
            .fillMaxSize()
            // El fondo va ANTES de safeDrawingPadding para que también pinte
            // detrás de la barra de estado
            .background(GrisClaro)
            .safeDrawingPadding()
    ) {
        EncabezadoCarrito(
            onVolver = onVolver,
            cantidadProductos = carrito.sumOf { it.cantidad }
        )

        if (carrito.isEmpty()) {
            // weight(1f): el estado vacío ocupa todo el espacio libre del medio
            EstadoCarritoVacio(
                onIrAInicio = onIrAInicio,
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                // contentPadding (y no padding) para que la sombra de las
                // tarjetas no se corte en los bordes
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(carrito, key = { it.producto.id }) { item ->
                    TarjetaProductoCarrito(
                        item = item,
                        onIncrementar = { onIncrementar(item.producto) },
                        onDecrementar = { onDecrementar(item.producto) },
                        onEliminar = { onEliminar(item.producto) }
                    )
                }
            }
        }

        ResumenYBoton(
            subtotal = subtotal,
            delivery = COSTO_DELIVERY,
            total = total,
            // Con el carrito vacío no tiene sentido pedir: botón desactivado
            hayProductos = carrito.isNotEmpty(),
            onContinuarPedido = onContinuarPedido
        )
    }
}

// Sub-composables PRIVADOS: solo los usa esta pantalla.

/** Flecha para volver, título y cuántas unidades hay en el carrito. */
@Composable
private fun EncabezadoCarrito(onVolver: () -> Unit, cantidadProductos: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onVolver) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = AzulTexto)
        }
        Text(
            text = "Mi carrito",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = AzulTexto
        )
        if (cantidadProductos > 0) {
            Spacer(Modifier.width(8.dp))
            Text(
                text = "($cantidadProductos)",
                style = MaterialTheme.typography.titleMedium,
                color = GrisTexto
            )
        }
    }
}

/**
 * Tarjeta blanca de un producto del carrito: imagen, nombre, precio por
 * unidad, selector de cantidad, botón eliminar y el subtotal de la línea.
 */
@Composable
private fun TarjetaProductoCarrito(
    item: ItemCarrito,
    onIncrementar: () -> Unit,
    onDecrementar: () -> Unit,
    onEliminar: () -> Unit
) {
    val forma = RoundedCornerShape(18.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = forma,
                ambientColor = AzulTexto.copy(alpha = 0.08f),
                spotColor = AzulTexto.copy(alpha = 0.14f)
            )
            .background(Blanco, forma)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Foto del producto (res/drawable)
        Image(
            painter = painterResource(item.producto.imagen),
            contentDescription = item.producto.nombre,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(VerdeBodega.copy(alpha = 0.12f))
        )

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.producto.nombre,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = AzulTexto,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            // "c/u" = cada uno: precio por unidad
            Text(
                text = "S/ %.2f c/u".format(item.producto.precio),
                style = MaterialTheme.typography.bodySmall,
                color = GrisTexto
            )
            Spacer(Modifier.height(6.dp))
            SelectorCantidad(
                cantidad = item.cantidad,
                onIncrementar = onIncrementar,
                onDecrementar = onDecrementar
            )
        }

        // A la derecha: eliminar arriba y el subtotal de la línea abajo
        Column(horizontalAlignment = Alignment.End) {
            IconButton(onClick = onEliminar) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar ${item.producto.nombre}",
                    tint = GrisTexto
                )
            }
            Text(
                text = "S/ %.2f".format(item.producto.precio * item.cantidad),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = VerdeBodega
            )
        }
    }
}

/**
 * Estado vacío: ícono grande del carrito, mensaje y botón para volver
 * a Inicio (avisa hacia arriba; ClienteApp decide cómo navegar).
 */
@Composable
private fun EstadoCarritoVacio(onIrAInicio: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
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
                imageVector = Icons.Default.RemoveShoppingCart,
                contentDescription = null, // decorativo: el texto lo explica
                tint = VerdeBodega,
                modifier = Modifier.size(56.dp)
            )
        }
        Spacer(Modifier.height(20.dp))
        Text(
            text = "Tu carrito está vacío",
            style = MaterialTheme.typography.titleMedium,
            color = AzulTexto
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Agrega productos desde Inicio o Categorías para hacer tu pedido.",
            style = MaterialTheme.typography.bodyMedium,
            color = GrisTexto,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        BotonSecundario(texto = "Volver al inicio", onClick = onIrAInicio)
    }
}

/**
 * Panel blanco inferior (esquinas de arriba redondeadas) con los montos
 * y el botón "Continuar pedido".
 * @param hayProductos si es false (carrito vacío) se ocultan los montos
 *        y el botón queda desactivado
 */
@Composable
private fun ResumenYBoton(
    subtotal: Double,
    delivery: Double,
    total: Double,
    hayProductos: Boolean,
    onContinuarPedido: () -> Unit
) {
    val forma = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, forma, spotColor = AzulTexto.copy(alpha = 0.12f))
            .background(Blanco, forma)
            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 12.dp)
    ) {
        // Los montos solo tienen sentido si hay algo que comprar
        if (hayProductos) {
            MontosResumen(subtotal = subtotal, delivery = delivery, total = total)
            Spacer(Modifier.height(16.dp))
        }

        BotonPrimario(
            texto = "Continuar pedido",
            onClick = onContinuarPedido,
            habilitado = hayProductos
        )
    }
}

/** Subtotal, delivery y total (en verde y más grande). */
@Composable
private fun MontosResumen(subtotal: Double, delivery: Double, total: Double) {
    Column {
        FilaResumen(etiqueta = "Subtotal", valor = subtotal)
        FilaResumen(etiqueta = "Costo de delivery", valor = delivery)

        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = GrisClaro)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total",
                style = MaterialTheme.typography.titleMedium,
                color = AzulTexto
            )
            Text(
                text = "S/ %.2f".format(total),
                style = MaterialTheme.typography.titleLarge,
                color = VerdeBodega
            )
        }
    }
}

/** Una fila "etiqueta ........ S/ 0.00" en gris. */
@Composable
private fun FilaResumen(etiqueta: String, valor: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = etiqueta, color = GrisTexto)
        Text(text = "S/ %.2f".format(valor), color = GrisTexto)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CarritoPreview() {
    val carritoEjemplo = listOf(
        ItemCarrito(listaProductosFake[4], 1), // Coca-Cola
        ItemCarrito(listaProductosFake[0], 2), // Arroz Costeño
        ItemCarrito(listaProductosFake[2], 1)  // Leche Gloria
    )
    BodegaTheme {
        CarritoScreen(
            carrito = carritoEjemplo,
            onVolver = {},
            onIncrementar = {},
            onDecrementar = {},
            onEliminar = {},
            onContinuarPedido = {},
            onIrAInicio = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CarritoVacioPreview() {
    BodegaTheme {
        CarritoScreen(
            carrito = emptyList(),
            onVolver = {},
            onIncrementar = {},
            onDecrementar = {},
            onEliminar = {},
            onContinuarPedido = {},
            onIrAInicio = {}
        )
    }
}
