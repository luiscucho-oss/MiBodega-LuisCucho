package com.tecsup.mibodega.ui.cliente.modelo

/**
 * Una cuenta de cliente. Se crea en el Registro (o es usuarioDeEjemplo, la cuenta
 * fija del código) y sus datos se muestran en Perfil.
 *
 * @param telefono junto con contrasena, es lo que se pide en Iniciar sesión.
 *        Se guarda sin espacios ("987654321").
 */
data class Usuario(
    val nombre: String,
    val telefono: String,
    val direccion: String,
    val referencia: String,
    val contrasena: String
)
