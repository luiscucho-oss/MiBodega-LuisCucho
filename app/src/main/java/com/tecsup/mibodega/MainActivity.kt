package com.tecsup.mibodega

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.tecsup.mibodega.ui.cliente.ClienteApp
import com.tecsup.mibodega.ui.theme.BodegaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // El modo oscuro vive aquí, arriba de BodegaTheme, porque es el tema el
            // que cambia los colores de TODA la app. ClienteApp solo lo recibe y
            // avisa cuando se toca el Switch de Perfil (state hoisting).
            // rememberSaveable (y no remember) para que no se pierda al girar el celular.
            var modoOscuro by rememberSaveable { mutableStateOf(false) }

            // enableEdgeToEdge() elige el color de los íconos de la barra de estado
            // (hora, batería) según el modo oscuro del SISTEMA, no el de la app.
            // Por eso se vuelve a llamar cada vez que cambia el Switch: íconos claros
            // sobre fondo oscuro y oscuros sobre fondo claro.
            LaunchedEffect(modoOscuro) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { modoOscuro },
                    navigationBarStyle = SystemBarStyle.auto(ScrimClaro, ScrimOscuro) { modoOscuro }
                )
            }

            BodegaTheme(modoOscuro = modoOscuro) {
                ClienteApp(
                    modoOscuro = modoOscuro,
                    onModoOscuroCambia = { modoOscuro = it }
                )
            }
        }
    }
}

// Velo detrás de los botones de navegación del sistema (los mismos colores
// que usa enableEdgeToEdge() por defecto)
private val ScrimClaro = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
private val ScrimOscuro = Color.argb(0x80, 0x1b, 0x1b, 0x1b)
