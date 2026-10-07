package com.tecsup.mibodega.ui.componentes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

/**
 * Input con label arriba (fuera del recuadro), como en los mockups
 * de Registro y Datos de entrega. Se usa en: Login, Registro, Datos de entrega.
 *
 * @param teclado tipo de teclado, ej. KeyboardType.Phone para el teléfono
 * @param esContrasena si es true, muestra puntos en lugar de las letras y usa
 *        el teclado de contraseña. Por defecto es false, así que las pantallas
 *        que ya usaban CampoTexto no cambian.
 * @param esError si es true, la etiqueta y el borde se pintan de rojo (color
 *        error del tema). Los formularios lo activan cuando se toca el botón
 *        con el campo vacío o con datos incorrectos.
 * @param mensajeError texto rojo que aparece debajo del campo mientras esError
 *        sea true. Con null solo se pinta de rojo, sin texto debajo.
 */
@Composable
fun CampoTexto(
    etiqueta: String,
    valor: String,
    onValorCambia: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    teclado: KeyboardType = KeyboardType.Text,
    esContrasena: Boolean = false,
    esError: Boolean = false,
    mensajeError: String? = "Este campo es obligatorio"
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            // La etiqueta también se pone roja para que el error se note a simple vista
            color = if (esError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground
        )
        OutlinedTextField(
            value = valor,
            onValueChange = onValorCambia,
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = placeholder?.let { { Text(it) } },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            // Para contraseñas se fuerza el teclado de contraseña (sin sugerencias ni autocorrector)
            keyboardOptions = KeyboardOptions(
                keyboardType = if (esContrasena) KeyboardType.Password else teclado
            ),
            // PasswordVisualTransformation dibuja "•" en vez de cada letra;
            // VisualTransformation.None muestra el texto tal cual
            visualTransformation = if (esContrasena) PasswordVisualTransformation() else VisualTransformation.None,
            // isError pinta el borde y el texto de ayuda con el color error (rojo)
            isError = esError,
            // supportingText es el texto de debajo del recuadro: solo existe mientras hay error
            supportingText = if (esError && mensajeError != null) {
                { Text(mensajeError) }
            } else {
                null
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                // Con error se mantiene el mismo fondo gris (por defecto sería transparente)
                errorContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedBorderColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}
