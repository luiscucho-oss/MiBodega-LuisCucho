package com.tecsup.mibodega.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val BodegaColorScheme = lightColorScheme(
    primary = VerdeBodega,
    onPrimary = Blanco,
    secondary = AzulEnlace,
    // Fondo celeste de la parte superior de Bienvenida
    tertiaryContainer = FondoClaro,
    background = Blanco,
    onBackground = AzulTexto,
    surface = Blanco,
    onSurface = AzulTexto,
    surfaceVariant = GrisClaro,
    onSurfaceVariant = GrisTexto,
    outline = GrisBorde,
    error = RojoPrecio
)

/**
 * Los mismos "roles" que el esquema claro, con colores oscuros. Como las pantallas
 * piden MaterialTheme.colorScheme.background, surfaceVariant, etc. (y no un color
 * fijo), cambian solas al pasar de un esquema al otro.
 */
private val BodegaColorSchemeOscuro = darkColorScheme(
    primary = VerdeBodega,
    onPrimary = Blanco,
    secondary = AzulEnlaceClaro,
    tertiaryContainer = AzulNoche,
    background = FondoOscuro,
    onBackground = TextoClaro,
    surface = SuperficieOscura,
    onSurface = TextoClaro,
    surfaceVariant = GrisOscuro,
    onSurfaceVariant = GrisTextoClaro,
    outline = GrisBordeOscuro,
    error = RojoClaro,
    // Barra inferior, diálogos y fondo del Switch apagado: grises neutros
    // (los de Material por defecto tiran a morado)
    surfaceContainer = SuperficieOscura,
    surfaceContainerHigh = SuperficieOscura,
    surfaceContainerHighest = GrisOscuro
)

/**
 * @param modoOscuro true = esquema oscuro. Lo decide el Switch de Perfil
 *        (el estado vive en MainActivity), no el modo oscuro del sistema.
 */
@Composable
fun BodegaTheme(
    modoOscuro: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (modoOscuro) BodegaColorSchemeOscuro else BodegaColorScheme,
        typography = BodegaTypography,
        content = content
    )
}
