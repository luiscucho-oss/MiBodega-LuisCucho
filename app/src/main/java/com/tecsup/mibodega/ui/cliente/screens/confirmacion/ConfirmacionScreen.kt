package com.tecsup.mibodega.ui.cliente.screens.confirmacion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.BotonSecundario
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.VerdeBodega

/**
 * Pantalla 7: Pedido confirmado (mockup "Cliente").
 * Es la última pantalla del flujo de compra. No tiene estado propio:
 * solo muestra el mensaje de éxito y avisa hacia arriba (onVolverAlInicio)
 * cuando el usuario quiere seguir comprando. ClienteApp decide cómo navegar.
 */
@Composable
fun ConfirmacionScreen(
    onVolverAlInicio: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Spacer con weight arriba y abajo: el mensaje queda centrado en la
        // parte libre de la pantalla y el botón se queda pegado abajo
        Spacer(Modifier.weight(1f))

        IconoExito()

        Spacer(Modifier.height(24.dp))

        Text(
            text = "¡Pedido confirmado!",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Gracias por tu compra. Estamos preparando tu pedido " +
                    "y te lo llevaremos a la dirección que indicaste.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.weight(1f))

        // Botón blanco con borde: BotonSecundario ya indica en su documentación
        // que se usa para "Volver al inicio" en esta pantalla
        BotonSecundario(
            texto = "Volver al inicio",
            onClick = onVolverAlInicio
        )

        Spacer(Modifier.height(24.dp))
    }
}

// Sub-composables PRIVADOS: solo los usa esta pantalla.

/** Check verde dentro de un círculo gris claro: indica que todo salió bien. */
@Composable
private fun IconoExito() {
    Icon(
        imageVector = Icons.Default.CheckCircle,
        // contentDescription para lectores de pantalla (accesibilidad)
        contentDescription = "Pedido exitoso",
        tint = VerdeBodega,
        modifier = Modifier
            .size(120.dp)
            .background(GrisClaro, CircleShape)
            .padding(12.dp)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ConfirmacionPreview() {
    BodegaTheme {
        ConfirmacionScreen(onVolverAlInicio = {})
    }
}
