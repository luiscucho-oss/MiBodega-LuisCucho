package com.tecsup.mibodega.ui.cliente.modelo

/**
 * Datos del cliente que usa la app. Se llenan en el Registro (o con
 * usuarioDeEjemplo si entra por Login sin registrarse) y se muestran en Perfil.
 */
data class Usuario(
    val nombre: String,
    val telefono: String,
    val direccion: String,
    val referencia: String
)
