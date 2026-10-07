package com.tecsup.mibodega.ui.cliente.screens.confirmacion

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.BotonSecundario
import com.tecsup.mibodega.ui.theme.AzulTexto
import com.tecsup.mibodega.ui.theme.Blanco
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.FondoClaro
import com.tecsup.mibodega.ui.theme.GrisTexto
import com.tecsup.mibodega.ui.theme.VerdeBodega

/**
 * Pantalla 7: Pedido confirmado (mockup "Cliente").
 * Es la última pantalla del flujo de compra. No tiene estado de negocio:
 * solo muestra el mensaje de éxito y avisa hacia arriba (onVolverAlInicio)
 * cuando el usuario quiere seguir comprando. ClienteApp decide cómo navegar.
 *
 * Animación: al entrar, el ícono de éxito "rebota" de tamaño 0 a 1
 * (animateFloatAsState con un resorte) y después el texto aparece
 * deslizándose hacia arriba (AnimatedVisibility).
 */
@Composable
fun ConfirmacionScreen(
    onVolverAlInicio: () -> Unit
) {
    // Empieza en false y pasa a true apenas se muestra la pantalla: ese
    // cambio es el que dispara las dos animaciones
    var mostrarContenido by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { mostrarContenido = true }

    // Escala del ícono: de 0 (invisible) a 1 (tamaño normal). El resorte con
    // rebote medio hace que "pase" un poco de 1 y vuelva, como un pequeño salto.
    val escalaIcono by animateFloatAsState(
        targetValue = if (mostrarContenido) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "escalaIcono"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            // Degradado celeste → blanco, igual que en Bienvenida
            .background(
                Brush.verticalGradient(
                    colors = listOf(FondoClaro, Blanco),
                    endY = 1200f
                )
            )
            .safeDrawingPadding()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Spacer con weight arriba y abajo: el mensaje queda centrado en la
        // parte libre de la pantalla y el botón se queda pegado abajo
        Spacer(Modifier.weight(1f))

        // graphicsLayer aplica la escala solo al dibujar (no cambia el tamaño
        // que ocupa en el layout, así el texto de abajo no "salta")
        IconoExito(
            modifier = Modifier.graphicsLayer {
                scaleX = escalaIcono
                scaleY = escalaIcono
            }
        )

        Spacer(Modifier.height(28.dp))

        // El texto aparece un poco después del ícono (delayMillis) con un
        // fundido y deslizándose desde abajo
        AnimatedVisibility(
            visible = mostrarContenido,
            enter = fadeIn(tween(durationMillis = 500, delayMillis = 300)) +
                    slideInVertically(tween(durationMillis = 500, delayMillis = 300)) { alto -> alto / 2 }
        ) {
            MensajeConfirmacion()
        }

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

/**
 * Check verde dentro de dos "halos" circulares de verde cada vez más suave:
 * da sensación de éxito y llama la atención al centro de la pantalla.
 */
@Composable
private fun IconoExito(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(170.dp)
            .background(VerdeBodega.copy(alpha = 0.08f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(130.dp)
                .background(VerdeBodega.copy(alpha = 0.16f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                // contentDescription para lectores de pantalla (accesibilidad)
                contentDescription = "Pedido exitoso",
                tint = VerdeBodega,
                modifier = Modifier.size(96.dp)
            )
        }
    }
}

/** Título, explicación y un aviso de dónde seguir el pedido. */
@Composable
private fun MensajeConfirmacion() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "¡Pedido confirmado!",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = AzulTexto,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Gracias por tu compra. Estamos preparando tu pedido " +
                    "y te lo llevaremos a la dirección que indicaste.",
            style = MaterialTheme.typography.bodyMedium,
            color = GrisTexto,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(20.dp))

        // Tarjeta verde muy suave y redondeada que indica dónde ver el estado
        // (verde y no blanca, porque el fondo de abajo ya es blanco)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(VerdeBodega.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Receipt,
                contentDescription = null,
                tint = VerdeBodega,
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = "Puedes seguir su estado en la pestaña Pedidos.",
                style = MaterialTheme.typography.bodySmall,
                color = AzulTexto
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ConfirmacionPreview() {
    BodegaTheme {
        ConfirmacionScreen(onVolverAlInicio = {})
    }
}
