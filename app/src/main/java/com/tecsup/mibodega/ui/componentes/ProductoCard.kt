package com.tecsup.mibodega.ui.componentes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.RojoPrecio
import com.tecsup.mibodega.ui.theme.VerdeBodega

/**
 * Tarjeta de producto usada en Inicio, Categorías y Favoritos.
 * Solo muestra datos y avisa cuando la tocan, cuando tocan "+" o el corazón;
 * no sabe nada de navegación, del carrito ni de la lista de favoritos.
 *
 * @param esFavorito corazón lleno (rojo) si es true, solo el borde si es false
 */
@Composable
fun ProductoCard(
    producto: Producto,
    esFavorito: Boolean,
    onClick: () -> Unit,
    onAgregar: () -> Unit,
    onFavoritoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            // Box: el corazón se dibuja ENCIMA de la foto, en la esquina superior derecha
            Box {
                // Foto del producto (res/drawable). Fit muestra el empaque completo sin recortarlo.
                // Las fotos tienen fondo blanco: el recuadro sigue gris claro en modo oscuro.
                Image(
                    painter = painterResource(producto.imagen),
                    contentDescription = producto.nombre,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.3f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(GrisClaro)
                )

                // El IconButton mide 48 dp (tamaño mínimo para el dedo); el círculo
                // blanco va en el Icon para que se vea de solo 30 dp
                IconButton(
                    onClick = onFavoritoClick,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = if (esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        // Dice qué hará el toque, para el lector de pantalla
                        contentDescription = if (esFavorito) {
                            "Quitar ${producto.nombre} de favoritos"
                        } else {
                            "Agregar ${producto.nombre} a favoritos"
                        },
                        tint = if (esFavorito) RojoPrecio else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .size(30.dp)
                            // Círculo casi opaco para que el corazón se vea sobre cualquier foto
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f), CircleShape)
                            .padding(6.dp)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "S/ %.2f".format(producto.precio),
                    style = MaterialTheme.typography.labelMedium,
                    color = VerdeBodega
                )
                IconButton(
                    onClick = onAgregar,
                    modifier = Modifier
                        .size(30.dp)
                        .background(VerdeBodega, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar ${producto.nombre}",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

