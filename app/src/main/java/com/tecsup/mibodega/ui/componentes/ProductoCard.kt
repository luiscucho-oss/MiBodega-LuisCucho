package com.tecsup.mibodega.ui.componentes

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
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.theme.AzulTexto
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.VerdeBodega

/**
 * Tarjeta de producto usada en el grid de Inicio y en las filas de Categorías.
 * Solo muestra datos y avisa cuando la tocan o cuando tocan "+";
 * no sabe nada de navegación ni del carrito.
 */
@Composable
fun ProductoCard(
    producto: Producto,
    onClick: () -> Unit,
    onAgregar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(18.dp)

    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            // Sombra suave: se usa AzulTexto casi transparente en vez de negro
            // para que la sombra sea difusa y no se vea "sucia"
            .shadow(
                elevation = 8.dp,
                shape = forma,
                ambientColor = AzulTexto.copy(alpha = 0.08f),
                spotColor = AzulTexto.copy(alpha = 0.16f)
            ),
        shape = forma,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        // La elevación propia de la Card en 0: la sombra ya la pone .shadow()
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Placeholder de imagen: reemplázalo por Image(painterResource(...))
            // cuando tengan las fotos reales de cada producto.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.25f)
                    .background(GrisClaro, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingBasket,
                    contentDescription = producto.nombre,
                    tint = VerdeBodega,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = AzulTexto,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "S/ %.2f".format(producto.precio),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = VerdeBodega
                )
                // IconButton siempre ocupa al menos 48 dp (área cómoda para el dedo).
                // Si el fondo verde se le pone a él, el círculo crece a 48 dp y se
                // sale del margen de la tarjeta. Por eso el círculo visible es un
                // Box de 34 dp DENTRO del botón: se ve pequeño, pero se toca fácil.
                IconButton(onClick = onAgregar) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(VerdeBodega, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Agregar ${producto.nombre}",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

