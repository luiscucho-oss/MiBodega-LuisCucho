# Mi Bodega — App Cliente

- **Alumno:** Luis Cucho
- **Docente:** Juan José León Suiyon

---

## Descripción

Mi Bodega es una app Android para comprar en una bodega de barrio y pedir delivery. El cliente se registra o inicia sesión, recorre el catálogo, filtra por categoría, busca productos, revisa el detalle, arma su carrito y confirma el pedido con sus datos de entrega.


## Tecnologías

- Kotlin 2.0.21
- Jetpack Compose con Material 3 (Compose BOM 2024.09.00)
- Navigation Compose 2.8.0
- Android Gradle Plugin 8.13.2 y Gradle 8.13
- compileSdk 36 · targetSdk 36 · minSdk 24

## Ramas

| Rama | Fase | Contenido |
|---|---|---|
| `main` | Fase 1 | App completa: Login y Registro → Inicio → Detalle → Carrito → Datos de entrega → Confirmación, barra inferior con 4 destinos y las pantallas Categorías, Pedidos y Perfil. |
| `mejora-ia` | Fase 2 | Mejora con IA: buscador que ignora tildes, busca también en la descripción y avisa cuando no hay resultados, más un rediseño visual de las pantallas. Los prompts están en [PROMPTS.md](https://github.com/luiscucho-oss/MiBodega-LuisCucho/blob/mejora-ia/PROMPTS.md). |


## Flujo de navegación

```mermaid
flowchart LR
    BI[Bienvenida] --> LO[Iniciar sesión]
    BI --> RE[Registro]
    LO --> IN[Inicio]
    RE --> IN
    IN -- productoId --> DE[Detalle]
    DE -- agregar al carrito --> IN
    IN --> CA[Carrito]
    CA --> EN[Datos de entrega]
    EN -- popUpTo Inicio --> CO[Confirmación]
    CO -- Volver al inicio --> IN
    IN -. barra inferior .-> CT[Categorías]
    IN -. barra inferior .-> PE[Pedidos]
    IN -. barra inferior .-> PF[Perfil]
    PF -- Cerrar sesión --> BI
```

## Estructura del proyecto

Cada pantalla está en su propio archivo:

```text
app/src/main/java/com/tecsup/mibodega/
├── MainActivity.kt
└── ui/
    ├── cliente/
    │   ├── ClienteApp.kt        NavHost y estado de la app (carrito, usuario y pedidos)
    │   ├── Rutas.kt             Rutas de navegación
    │   ├── modelo/              Producto, ItemCarrito, Pedido, Usuario y DatosFake
    │   └── screens/
    │       ├── bienvenida/      BienvenidaScreen.kt
    │       ├── login/           LoginScreen.kt
    │       ├── registro/        RegistroScreen.kt
    │       ├── inicio/          InicioScreen.kt
    │       ├── categorias/      CategoriasScreen.kt
    │       ├── detalle/         DetalleProductoScreen.kt
    │       ├── carrito/         CarritoScreen.kt
    │       ├── entrega/         DatosEntregaScreen.kt
    │       ├── confirmacion/    ConfirmacionScreen.kt
    │       ├── pedidos/         PedidosScreen.kt
    │       └── perfil/          PerfilScreen.kt
    ├── componentes/             BarraNavegacion, BotonPrimario, BotonSecundario,
    │                            CampoTexto, ProductoCard y SelectorCantidad
    └── theme/                   Color, Theme y Type
```

## Requisitos funcionales

| ID | Requisito | Pantalla | `main` | `mejora-ia` |
|---|---|---|:---:|---|
| RF-01 | Mostrar la bienvenida con opciones para registrarse, iniciar sesión y leer los términos y condiciones en un diálogo. | Bienvenida | ✅ | ✅ |
| RF-02 | Iniciar sesión con teléfono y contraseña. "Ingresar" se activa solo con los dos campos llenos y lleva a Inicio sin poder volver con "atrás". | Iniciar sesión | ✅ | ✅ Rediseño |
| RF-03 | Registrarse con nombre, teléfono, dirección y referencia. "Crear cuenta" se activa con los 4 campos llenos y guarda los datos del usuario. | Registro | ✅ | ✅ |
| RF-04 | Mostrar el catálogo de productos y la cantidad de productos del carrito en la barra superior. | Inicio | ✅ | ✅ Saludo con el nombre, banner y tarjetas con sombra |
| RF-05 | Filtrar los productos por categoría con los chips Todos, Bebidas, Abarrotes y Snacks (`LazyRow`). | Inicio | ✅ | ✅ |
| RF-06 | Buscar productos en tiempo real, combinando la búsqueda con el filtro de categoría. | Inicio | ✅ Por nombre | ✅ Ignora tildes, busca también en la descripción y muestra "No se encontraron productos" |
| RF-07 | Ver el detalle del producto recibido por parámetro (`productoId`), elegir la cantidad, agregarlo al carrito y marcarlo como favorito. | Detalle | ✅ | ✅ El botón muestra el total según la cantidad |
| RF-08 | Ver el carrito (`LazyColumn`) y sumar, restar o eliminar productos. | Carrito | ✅ | ✅ Estado de carrito vacío y "Continuar pedido" desactivado |
| RF-09 | Calcular el total automáticamente: subtotal + delivery (S/ 4.00), sin botón de recalcular. | Carrito | ✅ | ✅ |
| RF-10 | Ingresar los datos de entrega y ver el resumen del pedido. "Confirmar pedido" se activa con los campos completos. | Datos de entrega | ✅ | ✅ El formulario se llena con los datos del usuario |
| RF-11 | Confirmar el pedido: se guarda, se vacía el carrito y "atrás" ya no vuelve al carrito (`popUpTo`). | Confirmación | ✅ | ✅ Ícono de éxito animado |
| RF-12 | Navegar entre Inicio, Categorías, Pedidos y Perfil con la barra inferior. | Barra inferior | ✅ | ✅ |
| RF-13 | Ver los productos agrupados en una fila por categoría. | Categorías | ✅ | ✅ Ícono, color y contador por categoría |
| RF-14 | Ver los pedidos confirmados durante la sesión. | Pedidos | ✅ | ✅ Tarjetas con fecha y estado "En camino" |
| RF-15 | Ver los datos del usuario y cerrar sesión, limpiando la navegación. | Perfil | ✅ | ✅ Avatar, resumen de pedidos y confirmación al salir |

## Capturas

### Fase 1 — rama `main`

Las tarjetas, el detalle y el carrito muestran la foto real de cada producto.

| Bienvenida | Iniciar sesión | Registro |
|:---:|:---:|:---:|
| <img src="capturas/main/01-bienvenida.png" width="220"> | <img src="capturas/main/02-login.png" width="220"> | <img src="capturas/main/03-registro.png" width="220"> |
| **Inicio** | **Filtro por categoría** | **Detalle del producto** |
| <img src="capturas/main/04-inicio.png" width="220"> | <img src="capturas/main/05-inicio-categoria.png" width="220"> | <img src="capturas/main/06-detalle.png" width="220"> |
| **Carrito** | **Datos de entrega** | **Confirmación** |
| <img src="capturas/main/07-carrito.png" width="220"> | <img src="capturas/main/08-datos-entrega.png" width="220"> | <img src="capturas/main/09-confirmacion.png" width="220"> |
| **Categorías** | **Pedidos** | **Perfil** |
| <img src="capturas/main/10-categorias.png" width="220"> | <img src="capturas/main/11-pedidos.png" width="220"> | <img src="capturas/main/12-perfil.png" width="220"> |

### Fase 2 — rama `mejora-ia`

| Iniciar sesión | Inicio | Búsqueda sin tildes ("costeno") |
|:---:|:---:|:---:|
| <img src="capturas/mejora-ia/01-login.png" width="220"> | <img src="capturas/mejora-ia/02-inicio.png" width="220"> | <img src="capturas/mejora-ia/03-busqueda-tildes.png" width="220"> |
| **Búsqueda en la descripción ("chocolate")** | **Sin resultados ("arroz" en Bebidas)** | **Categorías** |
| <img src="capturas/mejora-ia/04-busqueda-descripcion.png" width="220"> | <img src="capturas/mejora-ia/05-sin-resultados.png" width="220"> | <img src="capturas/mejora-ia/06-categorias.png" width="220"> |
| **Detalle del producto** | **Carrito vacío** | **Datos de entrega** |
| <img src="capturas/mejora-ia/07-detalle.png" width="220"> | <img src="capturas/mejora-ia/08-carrito-vacio.png" width="220"> | <img src="capturas/mejora-ia/09-datos-entrega.png" width="220"> |
| **Confirmación** | **Pedidos** | **Perfil** |
| <img src="capturas/mejora-ia/10-confirmacion.png" width="220"> | <img src="capturas/mejora-ia/11-pedidos.png" width="220"> | <img src="capturas/mejora-ia/12-perfil.png" width="220"> |

## Cómo ejecutar

1. Clona el repositorio:
   ```bash
   git clone https://github.com/luiscucho-oss/MiBodega-LuisCucho.git
   ```
2. En Android Studio abre la carpeta que contiene `settings.gradle.kts`.
3. Elige la rama (`git checkout main` o `git checkout mejora-ia`) y presiona **Run**. Cada vez que cambies de rama, vuelve a presionar **Run** para reinstalar la app.
4. Para probar sin registrarte, en **Iniciar sesión** escribe cualquier teléfono y contraseña: la app usa datos de ejemplo.

> Gradle 8.13 funciona con JDK 17 a 23. Si Android Studio usa JDK 25, cambia el Gradle JDK a la versión 21.

## Preguntas de reflexión

**1. ¿Por qué `Producto.kt` y `MainActivity.kt` se entregaron completos, y las pantallas no? ¿Qué tienen en común los archivos que sí se dejaron como esqueleto?**

Porque son la base que comparten todas las pantallas. `Producto.kt` fija la forma de los datos (id, nombre, descripción, precio y categoría) y `MainActivity.kt` solo arranca la app con el tema y `ClienteApp()`. Si cada uno los escribiera a su manera, las pantallas no encajarían entre sí. Las pantallas, en cambio, son donde se practica lo que pide la tarea. Los archivos que se dejaron como esqueleto (`DatosEntregaScreen.kt`, `ConfirmacionScreen.kt` y los `TODO` de `ClienteApp.kt` y `DetalleProductoScreen.kt`) tienen en común que son interfaz con estado y navegación: formularios con `remember`, botones que se activan según ese estado, navegación con `popUpTo` y lambdas que avisan a `ClienteApp` (state hoisting).

**2. ¿Cómo lograste que el filtro de categoría (`LazyRow`) y el cálculo del carrito reaccionen automáticamente sin que tú "actualices" nada a mano?**

Guardando los datos en estado de Compose. La categoría elegida y el texto del buscador son `mutableStateOf` dentro de `InicioScreen`, y `productosFiltrados` se calcula a partir de ellos: al tocar un chip cambia el estado y Compose vuelve a dibujar la lista ya filtrada. El carrito vive en `ClienteApp` como `mutableStateOf<List<ItemCarrito>>` y nunca se modifica la lista: se reemplaza por una nueva (`map`, `filterNot` o `+`), así Compose detecta el cambio. El subtotal y el total no se guardan en ningún lado: se calculan con `sumOf` cada vez que se dibuja la pantalla, por eso no hace falta un botón de "recalcular".

**3. ¿Qué diferencia notaste entre `navigate()` normal (Inicio → Detalle) y el que usa `popUpTo` (Datos de entrega → Confirmación)?**

Con `navigate()` normal la pantalla nueva se apila encima de la anterior: desde Detalle, "atrás" vuelve a Inicio, que es lo esperado. En Confirmación eso sería un error, porque "atrás" regresaría al formulario y al carrito de un pedido ya hecho. Con `popUpTo(Rutas.INICIO)` se sacan de la pila Carrito y Datos de entrega antes de abrir Confirmación, así la pila queda Inicio → Confirmación. La misma idea se usa en "Volver al inicio" (con `launchSingleTop` para no crear otro Inicio), al entrar desde Login o Registro (`popUpTo(BIENVENIDA) { inclusive = true }`) y al cerrar sesión, donde se limpia toda la pila.

**4. ¿Qué tuviste que corregir del código que te generó la IA para el buscador en tiempo real?**

La lógica del buscador funcionó a la primera: `normalizarTexto()` quita las tildes con `Normalizer`, pasa todo a minúsculas y se aplica al texto buscado, al nombre y a la descripción. Lo que sí ajusté al probarlo en el emulador fue la parte visual: el mensaje "No se encontraron productos" se puso arriba para que el teclado no lo tape, y en el rediseño el banner de promoción se oculta mientras se busca para que los resultados aparezcan primero. También comprobé que la búsqueda y el filtro de categoría siguieran funcionando juntos después de traer la barra de navegación desde `main`. Fuera del buscador, sí hubo que corregir el botón "+" de las tarjetas, que se salía del borde porque `IconButton` ocupa como mínimo 48 dp.

**5. Compara el `NavigationDrawer` del Laboratorio 6 con el `NavigationBar` de esta tarea: ¿en qué caso usarías cada uno en un proyecto propio?**

El `NavigationDrawer` es un menú lateral que se abre con un botón o deslizando: conviene cuando hay muchos destinos o secciones que se usan poco (ajustes, ayuda, cuenta) y en pantallas grandes. El `NavigationBar` muestra de 3 a 5 destinos principales siempre visibles y a un toque. En Mi Bodega usé `NavigationBar` porque Inicio, Categorías, Pedidos y Perfil se usan todo el tiempo. En un proyecto propio usaría `NavigationBar` para las secciones principales de una app de uso diario, y `NavigationDrawer` cuando haya más de cinco secciones, como en una app de administración con muchas opciones.

## Observaciones

1. El carrito, el usuario y los pedidos viven en memoria (en `ClienteApp`): al cerrar la app se pierden, y quien entra por "Iniciar sesión" sin haberse registrado ve datos de ejemplo. Para una app real haría falta guardarlos con Room o DataStore, o en un servidor.
2. El proyecto usaba Gradle 8.13, que no funciona con el JDK 25 que trae Android Studio. Se actualizó el Gradle Wrapper a 9.4.1, que sí lo soporta, así que el proyecto sincroniza y compila al clonarlo sin cambiar el Gradle JDK.
3. Las fotos de los productos están en `res/drawable` (`producto_*.jpg`) y se obtuvieron de [Open Food Facts](https://world.openfoodfacts.org), una base de datos abierta de productos. Cada `Producto` guarda su foto en el campo `imagen`, y al estar dentro de la app se ven sin conexión a internet.

## Conclusiones

1. Separar el estado (en `ClienteApp`) de las pantallas, que solo reciben datos y lambdas, hizo que agregar Login, Categorías, Pedidos y Perfil fuera ordenado: cada pantalla es independiente y se puede revisar con `@Preview`. Además, controlar la pila con `popUpTo`, `launchSingleTop`, `saveState` y `restoreState` hace que el botón "atrás" se comporte como espera el usuario.
2. La IA ayudó a avanzar rápido con el buscador y el rediseño, pero no reemplaza la revisión: hubo que probar cada cambio en el emulador para encontrar detalles como botones que se salían de la tarjeta o textos que el teclado tapaba.
