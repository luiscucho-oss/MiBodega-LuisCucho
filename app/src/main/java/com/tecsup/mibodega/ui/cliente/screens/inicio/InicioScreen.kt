package com.tecsup.mibodega.ui.cliente.screens.inicio

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaCategorias
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.componentes.BarraNavegacion
import com.tecsup.mibodega.ui.componentes.PestanaNavegacion
import com.tecsup.mibodega.ui.componentes.ProductoCard
import com.tecsup.mibodega.ui.theme.AzulTexto
import com.tecsup.mibodega.ui.theme.Blanco
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisBorde
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.GrisTexto
import com.tecsup.mibodega.ui.theme.VerdeBodega
import com.tecsup.mibodega.ui.theme.VerdeOscuro
import java.text.Normalizer

/**
 * Pantalla 3: Inicio / Productos (mockup "Cliente").
 * La más completa: Scaffold (topBar con saludo + bottomBar), buscador fijo y
 * una LazyVerticalGrid que contiene el banner, los chips de categoría y los productos.
 *
 * @param productos lista completa (fake por ahora, luego vendrá de un ViewModel)
 * @param nombreUsuario para el saludo "Hola, <nombre>" de la barra superior
 * @param cantidadCarrito para el badge del carrito en la topBar
 * @param onNavegar avisa qué pestaña de la barra inferior se tocó
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    productos: List<Producto> = listaProductosFake,
    nombreUsuario: String,
    cantidadCarrito: Int,
    onVerCarrito: () -> Unit,
    onProductoClick: (Producto) -> Unit,
    onAgregarProducto: (Producto) -> Unit,
    onNavegar: (PestanaNavegacion) -> Unit
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
        // Fondo gris muy claro: así las tarjetas blancas con sombra resaltan
        containerColor = GrisClaro,
        topBar = {
            TopAppBar(
                // En lugar de "Mi Bodega", un saludo personal con el nombre del usuario
                title = { SaludoUsuario(nombreUsuario = nombreUsuario) },
                actions = {
                    IconButton(onClick = onVerCarrito) {
                        BadgedBox(
                            badge = {
                                if (cantidadCarrito > 0) {
                                    Badge { Text("$cantidadCarrito") }
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = "Carrito",
                                tint = AzulTexto
                            )
                        }
                    }
                },
                // La barra superior usa el mismo fondo que la pantalla (sin "corte")
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GrisClaro)
            )
        },
        // Barra inferior compartida: aquí la pestaña activa es INICIO y, al
        // tocar otra, solo se avisa hacia arriba con onNavegar
        bottomBar = {
            BarraNavegacion(
                pestanaActual = PestanaNavegacion.INICIO,
                onPestanaClick = onNavegar
            )
        }
    ) { paddingInterno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
        ) {
            // El buscador queda FIJO arriba (fuera de la grilla), así no se
            // pierde de vista al bajar por la lista de productos
            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 4.dp),
                placeholder = { Text("Buscar productos...", color = GrisTexto) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = GrisTexto) },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Blanco,
                    focusedContainerColor = Blanco,
                    unfocusedBorderColor = GrisBorde,
                    focusedBorderColor = VerdeBodega
                )
            )

            // Todo lo demás va DENTRO de la grilla para que banner, chips y
            // productos se desplacen juntos con un solo scroll
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                // El margen va en contentPadding (y no en padding) para que la
                // sombra de las tarjetas de los bordes no se corte
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 24.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                // GridItemSpan(maxLineSpan) = este elemento ocupa las 2 columnas

                // El banner se oculta mientras el usuario busca, para que los
                // resultados aparezcan arriba sin tener que bajar
                if (textoBusqueda.isBlank()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        BannerPromocion()
                    }
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        text = "Productos destacados",
                        style = MaterialTheme.typography.titleMedium,
                        color = AzulTexto
                    )
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(listaCategorias) { categoria ->
                            ChipCategoria(
                                texto = categoria,
                                seleccionado = categoria == categoriaSeleccionada,
                                onClick = { categoriaSeleccionada = categoria }
                            )
                        }
                    }
                }

                // Si ningún producto pasa los filtros (búsqueda + categoría), se muestra
                // un mensaje en lugar de dejar la pantalla en blanco. Como
                // productosFiltrados se recalcula al escribir o al cambiar de categoría,
                // el mensaje aparece y desaparece solo.
                if (productosFiltrados.isEmpty()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        MensajeSinResultados()
                    }
                } else {
                    // key = id: Compose reconoce cada tarjeta aunque cambie el filtro
                    items(productosFiltrados, key = { it.id }) { producto ->
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

/**
 * Estado vacío del buscador: ícono de lupa tachada + mensaje + sugerencia.
 * Va cerca de la parte superior (no al centro) para que el teclado
 * abierto no lo tape mientras el usuario sigue escribiendo.
 */
@Composable
private fun MensajeSinResultados() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.SearchOff,
            contentDescription = null, // decorativo: el texto de abajo ya lo explica
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(56.dp)
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "No se encontraron productos",
            style = MaterialTheme.typography.titleMedium,
            color = AzulTexto,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Prueba con otra palabra o elige otra categoría",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Saludo de la barra superior. Usa solo el primer nombre ("Luis Cucho" → "Luis")
 * para que sea corto y cercano.
 */
@Composable
private fun SaludoUsuario(nombreUsuario: String) {
    val primerNombre = nombreUsuario.trim().substringBefore(" ")
    Column {
        Text(
            text = "Hola, $primerNombre",
            style = MaterialTheme.typography.titleLarge,
            color = AzulTexto
        )
        Text(
            text = "¿Qué vas a pedir hoy?",
            style = MaterialTheme.typography.bodySmall,
            color = GrisTexto
        )
    }
}

/**
 * Banner de promoción: fondo verde (degradado de VerdeBodega a VerdeOscuro)
 * con esquinas redondeadas, un mensaje y un ícono grande de delivery.
 */
@Composable
private fun BannerPromocion() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            // clip recorta el fondo con las esquinas redondeadas
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.horizontalGradient(listOf(VerdeBodega, VerdeOscuro)))
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // weight(1f): el texto ocupa todo el ancho que deja libre el ícono
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "¡Tu bodega a un toque!",
                style = MaterialTheme.typography.titleMedium,
                color = Blanco
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Pide tus productos de siempre y te los llevamos a tu puerta.",
                style = MaterialTheme.typography.bodySmall,
                color = Blanco
            )
            Spacer(Modifier.height(10.dp))
            // "Píldora" blanca con el costo del delivery
            Text(
                text = "Delivery S/ 4.00",
                style = MaterialTheme.typography.labelMedium,
                color = VerdeOscuro,
                modifier = Modifier
                    .background(Blanco, RoundedCornerShape(50))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
        Spacer(Modifier.width(12.dp))
        Icon(
            imageVector = Icons.Default.LocalShipping,
            contentDescription = null, // decorativo
            tint = Blanco,
            modifier = Modifier.size(64.dp)
        )
    }
}

/**
 * Chip de categoría más claro:
 * - Seleccionado: fondo verde, texto blanco y un check, para que se note cuál está activo.
 * - No seleccionado: fondo blanco con borde gris y texto oscuro (buen contraste).
 */
@Composable
private fun ChipCategoria(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    val fondo = if (seleccionado) VerdeBodega else Blanco
    val colorTexto = if (seleccionado) Blanco else AzulTexto
    val colorBorde = if (seleccionado) VerdeBodega else GrisBorde
    // RoundedCornerShape(50) = 50 % del alto: bordes totalmente redondos
    val forma = RoundedCornerShape(50)

    Row(
        modifier = Modifier
            .clip(forma)
            .background(fondo)
            .border(1.dp, colorBorde, forma)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (seleccionado) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Blanco,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(4.dp))
        }
        Text(text = texto, color = colorTexto, fontWeight = FontWeight.SemiBold)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun InicioPreview() {
    BodegaTheme {
        InicioScreen(
            nombreUsuario = "Luis Cucho",
            cantidadCarrito = 3,
            onVerCarrito = {},
            onProductoClick = {},
            onAgregarProducto = {},
            onNavegar = {}
        )
    }
}