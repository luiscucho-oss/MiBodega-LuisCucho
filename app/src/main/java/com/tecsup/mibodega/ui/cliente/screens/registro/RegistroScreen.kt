package com.tecsup.mibodega.ui.cliente.screens.registro

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.CampoTexto
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.VerdeBodega

/**
 * Pantalla 2: Registro de datos (mockup "Cliente").
 * Guarda su propio estado de formulario (remember) porque solo esta
 * pantalla lo necesita. Al enviar, entrega los datos ya listos.
 */
@Composable
fun RegistroScreen(
    onVolver: () -> Unit,
    onCrearCuenta: (nombre: String, telefono: String, direccion: String, referencia: String) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var referencia by remember { mutableStateOf("") }

    // Como estos datos se guardan en ClienteApp y se muestran en Perfil,
    // no se puede crear la cuenta con algún campo vacío
    // (isNotBlank: un campo con solo espacios cuenta como vacío)
    val camposCompletos = listOf(nombre, telefono, direccion, referencia).all { it.isNotBlank() }

    // Pasa a true la primera vez que se toca "Crear cuenta". Desde ahí, cada campo
    // vacío se marca en rojo; al escribir en él, el rojo se quita solo porque
    // esError se vuelve a calcular en cada recomposición.
    var intentoCrear by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        EncabezadoRegistro(onVolver = onVolver)

        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Foto de perfil",
                tint = VerdeBodega,
                modifier = Modifier
                    .size(84.dp)
                    .background(GrisClaro, CircleShape)
                    .padding(4.dp)
            )
        }

        Spacer(Modifier.height(28.dp))

        CampoTexto(
            etiqueta = "Nombre completo",
            valor = nombre,
            onValorCambia = { nombre = it },
            esError = intentoCrear && nombre.isBlank(),
            placeholder = "Juan Pérez"
        )
        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Teléfono",
            valor = telefono,
            onValorCambia = { telefono = it },
            esError = intentoCrear && telefono.isBlank(),
            placeholder = "987 654 321",
            teclado = KeyboardType.Phone
        )
        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Dirección de entrega",
            valor = direccion,
            onValorCambia = { direccion = it },
            esError = intentoCrear && direccion.isBlank(),
            placeholder = "Av. Los Olivos 123"
        )
        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Referencia",
            valor = referencia,
            onValorCambia = { referencia = it },
            esError = intentoCrear && referencia.isBlank(),
            placeholder = "Frente al parque"
        )

        Spacer(Modifier.height(28.dp))

        BotonPrimario(
            texto = "Crear cuenta",
            onClick = {
                if (camposCompletos) {
                    // trim(): se envían los datos sin espacios sobrantes al inicio o al final
                    onCrearCuenta(nombre.trim(), telefono.trim(), direccion.trim(), referencia.trim())
                } else {
                    // No se avanza: solo se marcan en rojo los campos vacíos
                    intentoCrear = true
                }
            }
        )

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun EncabezadoRegistro(onVolver: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = onVolver,
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
        }
        Text(
            text = "Crear cuenta",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f, fill = false)
        )
        Spacer(Modifier.size(48.dp)) // balancea el ancho del ícono de la izquierda
    }
    Text(
        text = "Completa tus datos para continuar",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.fillMaxWidth(),
        textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RegistroPreview() {
    BodegaTheme {
        RegistroScreen(onVolver = {}, onCrearCuenta = { _, _, _, _ -> })
    }
}

