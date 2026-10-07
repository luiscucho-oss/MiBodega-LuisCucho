# Prompts usados en la rama mejora-ia

## Prompt 1: mejoras del buscador

```text
Mejora el buscador de InicioScreen SIN romper el filtro de categoría (ambos deben funcionar
juntos):
a) Que ignore tildes y mayúsculas ("cafe" encuentra "Café").
b) Que busque también en la descripción del producto.
c) Que muestre "No se encontraron productos" cuando la lista filtrada esté vacía.
```

## Prompt 2: mejoras visuales

```text
Mejora el diseño de la app sin romper el buscador ni el filtro de categoría. Las pantallas no
navegan solas ni modifican estado global: reciben datos y lambdas desde ClienteApp. Reutiliza
CampoTexto, BotonPrimario, BotonSecundario, ProductoCard y los colores de ui/theme (VerdeBodega,
VerdeOscuro, AzulTexto, GrisTexto, RojoPrecio, GrisClaro, FondoClaro). Nombres en español y
comentarios en cada parte nueva. Haz estas mejoras:
1. InicioScreen: saludo con el nombre del usuario ("Hola, <nombre>"), banner de promoción con
   fondo VerdeBodega y esquinas redondeadas, chips de categoría más claros y tarjetas de producto
   con sombra suave y mejor espaciado.
2. CategoriasScreen: encabezado de cada categoría con ícono, color propio y contador de productos;
   tarjetas más atractivas.
3. PedidosScreen: tarjetas con número de pedido, fecha, chip de estado ("En camino"), lista
   resumida de productos y total destacado; estado vacío con ícono grande y botón "Ir a comprar".
4. PerfilScreen: avatar circular con las iniciales, tarjeta con los datos del usuario, resumen con
   cantidad de pedidos y total gastado, y "Cerrar sesión" con diálogo de confirmación.
5. Carrito vacío: deshabilita "Continuar pedido" cuando el carrito está vacío y muestra un estado
   vacío con ícono, "Tu carrito está vacío" y botón para volver a Inicio.
6. DatosEntregaScreen: rellena automáticamente el formulario con los datos del usuario guardados.
7. Mejora visual de LoginScreen, DetalleProductoScreen, CarritoScreen y ConfirmacionScreen:
   tarjetas redondeadas, títulos en AzulTexto, textos secundarios en GrisTexto, precios en
   RojoPrecio o VerdeBodega, espaciado consistente y una animación en el ícono de éxito de la
   confirmación (AnimatedVisibility o animateFloatAsState).
```
