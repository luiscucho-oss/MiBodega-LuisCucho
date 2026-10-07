package com.tecsup.mibodega.ui.componentes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.tecsup.mibodega.ui.theme.VerdeBodega

/**
 * Las 4 pestañas de la barra inferior, cada una con su texto e ícono.
 * Es un enum (y no Strings sueltos) para que el compilador avise si
 * alguna pestaña se queda sin manejar en un "when".
 */
enum class PestanaNavegacion(val etiqueta: String, val icono: ImageVector) {
    INICIO("Inicio", Icons.Default.Home),
    CATEGORIAS("Categorías", Icons.AutoMirrored.Filled.List),
    PEDIDOS("Pedidos", Icons.Default.Receipt),
    PERFIL("Perfil", Icons.Default.Person)
}

/**
 * Barra de navegación inferior. Se usa en: Inicio, Categorías, Pedidos y Perfil.
 * No sabe navegar: solo marca la pestaña actual y avisa cuál tocaron
 * (onPestanaClick). ClienteApp decide a qué ruta ir (state hoisting).
 *
 * @param pestanaActual la pestaña de la pantalla que está mostrando la barra
 */
@Composable
fun BarraNavegacion(
    pestanaActual: PestanaNavegacion,
    onPestanaClick: (PestanaNavegacion) -> Unit
) {
    NavigationBar {
        // entries = todas las pestañas del enum, en el orden en que se declararon
        PestanaNavegacion.entries.forEach { pestana ->
            NavigationBarItem(
                selected = pestana == pestanaActual,
                onClick = { onPestanaClick(pestana) },
                // contentDescription = null porque el label de abajo ya dice
                // el nombre (si no, el lector de pantalla lo leería dos veces)
                icon = { Icon(pestana.icono, contentDescription = null) },
                label = { Text(pestana.etiqueta) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = VerdeBodega,
                    selectedTextColor = VerdeBodega
                )
            )
        }
    }
}
