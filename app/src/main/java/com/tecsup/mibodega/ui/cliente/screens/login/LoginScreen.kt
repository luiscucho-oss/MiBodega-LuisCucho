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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.CampoTexto
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.VerdeBodega

/**
 * Pantalla de Iniciar sesión. Se abre desde Bienvenida con "Iniciar sesión"
 * y también después de crear una cuenta en Registro.
 * Guarda su propio estado de formulario (remember), igual que RegistroScreen,
 * porque solo esta pantalla necesita los textos mientras el usuario escribe.
 * No navega sola: avisa hacia arriba con onIngresar / onIrARegistro / onVolver
 * y ClienteApp decide a dónde ir (state hoisting).
 *
 * @param telefonoRegistrado teléfono de la cuenta que se acaba de crear en
 *        Registro: llena el campo y muestra el aviso "¡Cuenta creada!".
 *        Es null si se llegó desde Bienvenida.
 * @param onIngresar recibe el teléfono (sin espacios) y la contraseña, y devuelve
 *        true si coinciden con alguna cuenta. Si devuelve false, la pantalla
 *        muestra "Teléfono o contraseña incorrectos" y no avanza.
 */
@Composable
fun LoginScreen(
    telefonoRegistrado: String?,
    onVolver: () -> Unit,
    onIngresar: (telefono: String, contrasena: String) -> Boolean,
    onIrARegistro: () -> Unit
) {
    var telefono by remember { mutableStateOf(telefonoRegistrado ?: "") }
    var contrasena by remember { mutableStateOf("") }

    // isNotBlank() hace que un campo con solo espacios cuente como vacío
    val camposCompletos = telefono.isNotBlank() && contrasena.isNotBlank()

    // Pasa a true al tocar "Ingresar": desde ahí los campos vacíos se ven en rojo
    var intentoIngresar by remember { mutableStateOf(false) }

    // true si el teléfono y la contraseña no coinciden con ninguna cuenta.
    // Vuelve a false apenas el usuario corrige alguno de los dos campos.
    var datosIncorrectos by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        EncabezadoLogin(onVolver = onVolver)

        Spacer(Modifier.height(24.dp))

        // Ícono de candado en un círculo gris, como el avatar de RegistroScreen
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null, // decorativo
                tint = VerdeBodega,
                modifier = Modifier
                    .size(84.dp)
                    .background(GrisClaro, CircleShape)
                    .padding(20.dp)
            )
        }

        Spacer(Modifier.height(28.dp))

        // Solo aparece cuando se llega aquí justo después de registrarse
        if (telefonoRegistrado != null) {
            AvisoCuentaCreada()
            Spacer(Modifier.height(20.dp))
        }

        CampoTexto(
            etiqueta = "Teléfono",
            valor = telefono,
            onValorCambia = {
                telefono = it
                datosIncorrectos = false
            },
            placeholder = "987 654 321",
            teclado = KeyboardType.Phone,
            esError = (intentoIngresar && telefono.isBlank()) || datosIncorrectos,
            // Con datos incorrectos solo se pinta de rojo: el mensaje sale una sola
            // vez, debajo de la contraseña
            mensajeError = if (datosIncorrectos) null else "Ingresa tu teléfono"
        )
        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Contraseña",
            valor = contrasena,
            onValorCambia = {
                contrasena = it
                datosIncorrectos = false
            },
            placeholder = "••••••",
            // Oculta lo que se escribe (muestra puntos)
            esContrasena = true,
            esError = (intentoIngresar && contrasena.isBlank()) || datosIncorrectos,
            mensajeError = if (datosIncorrectos) "Teléfono o contraseña incorrectos" else "Ingresa tu contraseña"
        )

        Spacer(Modifier.height(28.dp))

        BotonPrimario(
            texto = "Ingresar",
            onClick = {
                intentoIngresar = true
                if (camposCompletos) {
                    // ClienteApp busca una cuenta con ese teléfono y esa contraseña.
                    // replace(" ", ""): "987 654 321" y "987654321" son el mismo teléfono
                    datosIncorrectos = !onIngresar(telefono.replace(" ", ""), contrasena)
                }
            }
        )

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
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
        }
        Text(
            text = "Iniciar sesión",
            style = MaterialTheme.typography.titleLarge
        )
    }
    Text(
        text = "Ingresa con tu teléfono y tu contraseña",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

/** Recuadro verde que confirma que la cuenta se creó y pide iniciar sesión. */
@Composable
private fun AvisoCuentaCreada() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(VerdeBodega.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null, // el texto de al lado ya lo explica
            tint = VerdeBodega
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = "¡Cuenta creada! Ahora ingresa con tu teléfono y tu contraseña.",
            style = MaterialTheme.typography.bodyMedium
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
            color = MaterialTheme.colorScheme.onSurfaceVariant
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
        LoginScreen(
            telefonoRegistrado = null,
            onVolver = {},
            onIngresar = { _, _ -> false },
            onIrARegistro = {}
        )
    }
}
