package com.tecsup.mibodega.ui.cliente.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.CampoTexto
import com.tecsup.mibodega.ui.theme.AzulTexto
import com.tecsup.mibodega.ui.theme.Blanco
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.GrisTexto
import com.tecsup.mibodega.ui.theme.VerdeBodega

/**
 * Pantalla de Iniciar sesión. Se abre desde Bienvenida con "Iniciar sesión".
 * Guarda su propio estado de formulario (remember), igual que RegistroScreen,
 * porque solo esta pantalla necesita los textos mientras el usuario escribe.
 * No navega sola: avisa hacia arriba con onIngresar / onIrARegistro / onVolver
 * y ClienteApp decide a dónde ir (state hoisting).
 *
 * Diseño: fondo gris claro, saludo con un candado verde y el formulario
 * dentro de una tarjeta blanca de esquinas redondeadas.
 *
 * @param onIngresar recibe el teléfono escrito. La contraseña no se envía hacia
 *        arriba porque todavía no hay un servidor que la valide: por ahora solo
 *        se exige que el campo no esté vacío.
 */
@Composable
fun LoginScreen(
    onVolver: () -> Unit,
    onIngresar: (telefono: String) -> Unit,
    onIrARegistro: () -> Unit
) {
    var telefono by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    // "Ingresar" solo se activa con ambos campos llenos. isNotBlank() hace
    // que un campo con solo espacios cuente como vacío.
    val camposCompletos = telefono.isNotBlank() && contrasena.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            // El fondo va ANTES de safeDrawingPadding para que también pinte
            // detrás de la barra de estado
            .background(GrisClaro)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        EncabezadoLogin(onVolver = onVolver)

        Spacer(Modifier.height(16.dp))

        SaludoLogin()

        Spacer(Modifier.height(24.dp))

        // Tarjeta blanca con el formulario
        val forma = RoundedCornerShape(24.dp)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 10.dp,
                    shape = forma,
                    ambientColor = AzulTexto.copy(alpha = 0.08f),
                    spotColor = AzulTexto.copy(alpha = 0.14f)
                ),
            shape = forma,
            colors = CardDefaults.cardColors(containerColor = Blanco)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                CampoTexto(
                    etiqueta = "Teléfono",
                    valor = telefono,
                    onValorCambia = { telefono = it },
                    placeholder = "987 654 321",
                    teclado = KeyboardType.Phone
                )
                Spacer(Modifier.height(16.dp))

                CampoTexto(
                    etiqueta = "Contraseña",
                    valor = contrasena,
                    onValorCambia = { contrasena = it },
                    placeholder = "••••••",
                    // Oculta lo que se escribe (muestra puntos)
                    esContrasena = true
                )

                Spacer(Modifier.height(24.dp))

                BotonPrimario(
                    texto = "Ingresar",
                    onClick = { onIngresar(telefono.trim()) },
                    habilitado = camposCompletos
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        EnlaceRegistro(onIrARegistro = onIrARegistro)

        Spacer(Modifier.height(24.dp))
    }
}

// Sub-composables PRIVADOS: solo los usa esta pantalla.

@Composable
private fun EncabezadoLogin(onVolver: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onVolver) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = AzulTexto)
        }
        Text(
            text = "Iniciar sesión",
            style = MaterialTheme.typography.titleLarge,
            color = AzulTexto
        )
    }
}

/** Candado blanco en un círculo verde + "¡Hola de nuevo!" + indicación. */
@Composable
private fun SaludoLogin() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .shadow(8.dp, CircleShape, spotColor = VerdeBodega.copy(alpha = 0.4f))
                .background(VerdeBodega, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null, // decorativo
                tint = Blanco,
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = "¡Hola de nuevo!",
            style = MaterialTheme.typography.titleLarge,
            color = AzulTexto
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Ingresa con tu teléfono y tu contraseña",
            style = MaterialTheme.typography.bodyMedium,
            color = GrisTexto,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * "¿No tienes cuenta? Regístrate": toda la fila es tocable (área más grande
 * para el dedo) y la palabra "Regístrate" va en verde para que parezca enlace.
 */
@Composable
private fun EnlaceRegistro(onIrARegistro: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onIrARegistro)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "¿No tienes cuenta? ",
            style = MaterialTheme.typography.bodyMedium,
            color = GrisTexto
        )
        Text(
            text = "Regístrate",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = VerdeBodega
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginPreview() {
    BodegaTheme {
        LoginScreen(onVolver = {}, onIngresar = {}, onIrARegistro = {})
    }
}
