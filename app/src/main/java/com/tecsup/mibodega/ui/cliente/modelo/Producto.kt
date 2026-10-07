package com.tecsup.mibodega.ui.cliente.modelo

import androidx.annotation.DrawableRes

data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val categoria: String,
    // Foto del producto guardada en res/drawable (ej. R.drawable.producto_arroz_costeno)
    @DrawableRes val imagen: Int
)
