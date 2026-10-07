package com.tecsup.mibodega.ui.cliente.modelo

/**
 * Un pedido ya confirmado. Se crea en ClienteApp al tocar "Confirmar pedido"
 * en Datos de entrega y se muestra en la pestaña Pedidos.
 *
 * @param numero número correlativo del pedido (1, 2, 3...)
 * @param productos copia de lo que tenía el carrito al confirmar
 * @param total subtotal + costo de la entrega (S/ 0.00 si es recojo en tienda)
 * @param tipoEntrega delivery o recojo en tienda
 * @param direccion a dónde se entrega (vacía si es recojo en tienda)
 */
data class Pedido(
    val numero: Int,
    val productos: List<ItemCarrito>,
    val total: Double,
    val tipoEntrega: TipoEntrega,
    val direccion: String
)
