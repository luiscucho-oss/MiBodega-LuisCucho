package com.tecsup.mibodega.ui.cliente

/**
 * Todas las rutas (destinos) del NavHost de la app cliente.
 * Vive en su propio archivo (y sin "private") para que cualquier parte
 * del paquete pueda usarlas sin escribir los textos a mano: si una ruta
 * cambia, se cambia solo aquí.
 */
object Rutas {
    // Flujo de entrada
    const val BIENVENIDA = "bienvenida"
    const val LOGIN = "login"
    const val REGISTRO = "registro"

    // Las 4 pestañas de la barra inferior
    const val INICIO = "inicio"
    const val CATEGORIAS = "categorias"
    const val PEDIDOS = "pedidos"
    const val PERFIL = "perfil"

    // Flujo de compra
    const val DETALLE = "detalle/{productoId}"
    const val CARRITO = "carrito"

    // Pantalla del formulario de entrega (nombre, teléfono, dirección y referencia)
    // y del resumen del pedido. Se llega aquí desde el carrito con "Continuar pedido".
    // No lleva argumentos en la ruta: el resumen se calcula en ClienteApp a partir
    // del carrito, que ya vive allá arriba.
    const val ENTREGA = "entrega"

    // Pantalla final de "¡Pedido confirmado!". Se llega aquí desde ENTREGA al tocar
    // "Confirmar pedido". Se navega con popUpTo para que, al presionar "atrás" en
    // esta pantalla, el usuario no regrese al carrito ni al formulario.
    const val CONFIRMACION = "confirmacion"

    // DETALLE lleva un argumento: esta función arma la ruta con el id real
    fun detalle(productoId: Int) = "detalle/$productoId"
}
