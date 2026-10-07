package com.tecsup.mibodega.ui.cliente.screens.bienvenida

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.R
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.BotonSecundario
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.VerdeBodega

/**
 * Pantalla 1: Registro / Login (mockup "Cliente").
 * No sabe navegar sola: recibe qué hacer por parámetro (callbacks).
 * Lo único que maneja ella misma es si el diálogo de Términos está
 * abierto o cerrado, porque es estado visual que solo esta pantalla usa.
 */
@Composable
fun BienvenidaScreen(
    onRegistrarse: () -> Unit,
    onIniciarSesion: () -> Unit
) {
    // true = el diálogo de Términos y Condiciones está visible
    var mostrarTerminos by remember { mutableStateOf(false) }

    // El diálogo se dibuja encima de la pantalla solo mientras mostrarTerminos sea true
    if (mostrarTerminos) {
        DialogoTerminos(onCerrar = { mostrarTerminos = false })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    // tertiaryContainer: celeste en modo claro y azul noche en modo oscuro
                    colors = listOf(MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.background),
                    endY = 900f
                )
            )
            .safeDrawingPadding()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))

        IlustracionBodega()

        Spacer(Modifier.height(16.dp))

        TituloMiBodega()

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Tus productos de siempre\nen la puerta de tu casa",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.weight(1f))

        BotonPrimario(
            texto = "Registrarme",
            subtexto = "con mi teléfono",
            icono = rememberVectorPainter(Icons.Default.Phone),
            onClick = onRegistrarse
        )

        Spacer(Modifier.height(12.dp))

        BotonSecundario(
            texto = "Iniciar sesión",
            onClick = onIniciarSesion
        )

        Spacer(Modifier.height(20.dp))

        // Tocar el enlace solo abre el diálogo (cambia el estado local)
        PieTerminos(onTerminos = { mostrarTerminos = true })

        Spacer(Modifier.height(24.dp))
    }
}

// Sub-composables PRIVADOS: solo los usa esta pantalla, por eso no van a "componentes".

@Composable
private fun IlustracionBodega() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ilustracion_bodega),
            contentDescription = "Ilustración de la bodega",
            modifier = Modifier.size(200.dp)
        )
    }
}

@Composable
private fun TituloMiBodega() {
    Text(
        text = buildAnnotatedString {
            append("Mi ")
            withStyle(SpanStyle(color = VerdeBodega)) { append("Bodega") }
        },
        style = MaterialTheme.typography.displayMedium,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
private fun PieTerminos(onTerminos: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Al continuar aceptas nuestros",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "Términos y Condiciones",
            style = MaterialTheme.typography.bodySmall,
            // secondary = el azul de enlace del tema (más claro en modo oscuro)
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.clickable(onClick = onTerminos)
        )
    }
}

/**
 * Diálogo con un resumen corto de los términos. Se cierra con "Entendido"
 * o tocando fuera del diálogo (onDismissRequest).
 */
@Composable
private fun DialogoTerminos(onCerrar: () -> Unit) {
    AlertDialog(
        onDismissRequest = onCerrar,
        title = { Text("Términos y Condiciones") },
        text = {
            Text(
                "Al usar Mi Bodega aceptas que tus datos (nombre, teléfono y dirección) " +
                        "se usen solo para preparar y entregar tus pedidos. Los precios " +
                        "pueden cambiar sin previo aviso y el pago se realiza al recibir " +
                        "el pedido."
            )
        },
        confirmButton = {
            TextButton(onClick = onCerrar) {
                Text("Entendido", color = VerdeBodega)
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun BienvenidaPreview() {
    BodegaTheme {
        BienvenidaScreen(onRegistrarse = {}, onIniciarSesion = {})
    }
}

