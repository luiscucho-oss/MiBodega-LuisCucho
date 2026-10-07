package com.tecsup.mibodega.ui.cliente.modelo

/**
 * Cómo recibe el cliente su pedido. Se elige con RadioButton en Datos de entrega.
 * Cada opción guarda su propio costo, así el total siempre se calcula igual:
 * subtotal + tipoEntrega.costo (S/ 4.00 con delivery, S/ 0.00 con recojo).
 *
 * @param descripcion texto corto que se muestra debajo de la opción
 */
enum class TipoEntrega(val etiqueta: String, val descripcion: String, val costo: Double) {
    DELIVERY("Delivery", "Te lo llevamos a tu dirección", 4.00),
    RECOJO_EN_TIENDA("Recojo en tienda", "Lo recoges tú en la bodega", 0.00)
}
