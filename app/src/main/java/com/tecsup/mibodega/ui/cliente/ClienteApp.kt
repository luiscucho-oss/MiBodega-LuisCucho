package com.tecsup.mibodega.ui.cliente

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.mibodega.ui.cliente.modelo.ItemCarrito
import com.tecsup.mibodega.ui.cliente.modelo.Pedido
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.TipoEntrega
import com.tecsup.mibodega.ui.cliente.modelo.Usuario
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.cliente.modelo.usuarioDeEjemplo
import com.tecsup.mibodega.ui.cliente.screens.bienvenida.BienvenidaScreen
import com.tecsup.mibodega.ui.cliente.screens.categorias.CategoriasScreen
import com.tecsup.mibodega.ui.cliente.screens.carrito.CarritoScreen
import com.tecsup.mibodega.ui.cliente.screens.confirmacion.ConfirmacionScreen
import com.tecsup.mibodega.ui.cliente.screens.detalle.DetalleProductoScreen
import com.tecsup.mibodega.ui.cliente.screens.entrega.DatosEntregaScreen
import com.tecsup.mibodega.ui.cliente.screens.favoritos.FavoritosScreen
import com.tecsup.mibodega.ui.cliente.screens.inicio.InicioScreen
import com.tecsup.mibodega.ui.cliente.screens.login.LoginScreen
import com.tecsup.mibodega.ui.cliente.screens.pedidos.PedidosScreen
import com.tecsup.mibodega.ui.cliente.screens.perfil.PerfilScreen
import com.tecsup.mibodega.ui.cliente.screens.registro.RegistroScreen
import com.tecsup.mibodega.ui.componentes.PestanaNavegacion

/**
 * "Director de orquesta" de la app cliente:
 * - Tiene el NavHost con las rutas de cada pantalla.
 * - Tiene el estado del carrito (List<ItemCarrito>), que se reparte
 *   hacia abajo a Inicio, Detalle, Carrito y Entrega, y el de favoritos.
 * Ninguna Screen navega sola ni modifica el carrito directamente:
 * todas reciben funciones (lambdas) desde aquí (state hoisting).
 * Las rutas están en Rutas.kt (mismo paquete, por eso no necesitan import).
 */
@Composable
fun ClienteApp() {
    val navController = rememberNavController()

    // El carrito vive aquí arriba, no en ninguna Screen.
    var carrito by remember { mutableStateOf<List<ItemCarrito>>(emptyList()) }

    // Cuentas con las que se puede iniciar sesión. Empieza con la cuenta fija del
    // código (usuarioDeEjemplo: 987654321 / 123456) y cada Registro agrega una.
    // Viven en memoria, así que las cuentas creadas se pierden al cerrar la app.
    val cuentas = remember { mutableStateListOf(usuarioDeEjemplo) }

    // Usuario que inició sesión. Se llena solo en Login.
    // null = todavía nadie ha entrado (se está en Bienvenida, Registro o Login).
    var usuario by remember { mutableStateOf<Usuario?>(null) }

    // Teléfono de la cuenta recién creada en Registro. Login lo usa para llenar
    // el campo y mostrar "¡Cuenta creada!". null = se llegó a Login desde Bienvenida.
    var telefonoRecienRegistrado by remember { mutableStateOf<String?>(null) }

    // Ids de los productos marcados con el corazón. Es un Set (y no una List)
    // porque un producto está o no está en favoritos: nunca se repite.
    var favoritos by remember { mutableStateOf<Set<Int>>(emptySet()) }

    // Pone o quita un producto de favoritos. Se define una sola vez aquí porque
    // lo usan Inicio, Categorías, Detalle y Favoritos. Igual que con el carrito,
    // no se modifica el Set: se reemplaza por uno nuevo (+ o -) y Compose lo detecta.
    val alternarFavorito: (Producto) -> Unit = { producto ->
        favoritos = if (producto.id in favoritos) favoritos - producto.id else favoritos + producto.id
    }

    // Delivery o recojo en tienda (RadioButton de Datos de entrega). Vive aquí
    // porque de esto depende el total que muestran Carrito y Datos de entrega,
    // y porque el pedido lo guarda al confirmarse.
    var tipoEntrega by remember { mutableStateOf(TipoEntrega.DELIVERY) }

    // Pedidos confirmados. mutableStateListOf es una lista "observable":
    // al hacer add(), Compose redibuja solo las pantallas que la leen (Pedidos).
    val pedidos = remember { mutableStateListOf<Pedido>() }

    NavHost(
        navController = navController,
        startDestination = Rutas.BIENVENIDA
    ) {
        composable(Rutas.BIENVENIDA) {
            BienvenidaScreen(
                onRegistrarse = { navController.navigate(Rutas.REGISTRO) },
                onIniciarSesion = {
                    telefonoRecienRegistrado = null
                    navController.navigate(Rutas.LOGIN)
                }
                // Términos y condiciones: el diálogo lo abre y lo cierra la misma
                // BienvenidaScreen, porque es estado visual que solo ella usa.
            )
        }

        composable(Rutas.LOGIN) {
            LoginScreen(
                telefonoRegistrado = telefonoRecienRegistrado,
                onVolver = { navController.popBackStack() },
                onIngresar = { telefono, contrasena ->
                    // Se busca una cuenta con ese teléfono Y esa contraseña: la cuenta
                    // fija del código o alguna creada en Registro
                    val cuenta = cuentas.find { it.telefono == telefono && it.contrasena == contrasena }

                    if (cuenta != null) {
                        usuario = cuenta
                        telefonoRecienRegistrado = null

                        // Pila antes:   Bienvenida → Login
                        // Pila después: Inicio
                        // popUpTo(BIENVENIDA) inclusive saca Bienvenida y Login de la pila:
                        // ya con la sesión iniciada, "atrás" en Inicio cierra la app en vez
                        // de volver a la pantalla de ingreso.
                        navController.navigate(Rutas.INICIO) {
                            popUpTo(Rutas.BIENVENIDA) { inclusive = true }
                        }
                    }

                    // Se le responde a LoginScreen si los datos eran correctos:
                    // con false muestra el error y se queda donde está
                    cuenta != null
                },
                // Si no tiene cuenta, va al formulario de registro
                onIrARegistro = { navController.navigate(Rutas.REGISTRO) }
            )
        }

        composable(Rutas.REGISTRO) {
            RegistroScreen(
                onVolver = { navController.popBackStack() },
                onCrearCuenta = { nuevoUsuario ->
                    // El teléfono es con lo que se inicia sesión: no puede haber dos
                    // cuentas con el mismo
                    val telefonoLibre = cuentas.none { it.telefono == nuevoUsuario.telefono }

                    if (telefonoLibre) {
                        // Se guarda la cuenta aquí arriba para que Login pueda validarla
                        // y, después, Perfil muestre sus datos
                        cuentas.add(nuevoUsuario)
                        telefonoRecienRegistrado = nuevoUsuario.telefono

                        // Registrarse NO inicia sesión: se va a Login para entrar con
                        // el teléfono y la contraseña que se acaban de crear.
                        // Pila antes:   Bienvenida → Registro  (o Bienvenida → Login → Registro)
                        // Pila después: Bienvenida → Login
                        // popUpTo(BIENVENIDA) saca todo lo que está encima de Bienvenida:
                        // "atrás" en Login vuelve a Bienvenida y no al formulario ya enviado.
                        navController.navigate(Rutas.LOGIN) {
                            popUpTo(Rutas.BIENVENIDA)
                        }
                    }

                    // Con false, RegistroScreen marca el teléfono en rojo
                    telefonoLibre
                }
            )
        }

        composable(Rutas.INICIO) {
            InicioScreen(
                cantidadCarrito = carrito.sumOf { it.cantidad },
                favoritos = favoritos,
                onVerCarrito = { navController.navigate(Rutas.CARRITO) },
                onVerFavoritos = { navController.navigate(Rutas.FAVORITOS) },
                onProductoClick = { producto ->
                    navController.navigate(Rutas.detalle(producto.id))
                },
                onAgregarProducto = { producto ->
                    carrito = agregarOSumarProducto(carrito, producto, 1)
                },
                onFavoritoClick = alternarFavorito,
                onNavegar = { pestana -> navController.navegarAPestana(pestana) }
            )
        }

        composable(Rutas.CATEGORIAS) {
            // Mismos callbacks que Inicio: ver detalle, agregar con "+", favorito y ver carrito
            CategoriasScreen(
                cantidadCarrito = carrito.sumOf { it.cantidad },
                favoritos = favoritos,
                onVerCarrito = { navController.navigate(Rutas.CARRITO) },
                onProductoClick = { producto ->
                    navController.navigate(Rutas.detalle(producto.id))
                },
                onAgregarProducto = { producto ->
                    carrito = agregarOSumarProducto(carrito, producto, 1)
                },
                onFavoritoClick = alternarFavorito,
                onNavegar = { pestana -> navController.navegarAPestana(pestana) }
            )
        }

        composable(Rutas.PEDIDOS) {
            PedidosScreen(
                pedidos = pedidos,
                onNavegar = { pestana -> navController.navegarAPestana(pestana) }
            )
        }

        composable(Rutas.PERFIL) {
            PerfilScreen(
                // Siempre hay usuario aquí (solo se entra por Login); el ejemplo
                // es solo un respaldo para que nunca llegue null
                usuario = usuario ?: usuarioDeEjemplo,
                onCerrarSesion = {
                    // Al salir, el carrito y los favoritos de esta sesión ya no sirven
                    // y ya no hay nadie con la sesión iniciada (las cuentas sí se conservan)
                    carrito = emptyList()
                    favoritos = emptySet()
                    tipoEntrega = TipoEntrega.DELIVERY
                    usuario = null

                    // popUpTo(graph.id) inclusive saca TODAS las pantallas de la pila
                    // (Inicio, Perfil...) y deja solo Bienvenida. Así, "atrás" en
                    // Bienvenida cierra la app en vez de volver a una sesión cerrada.
                    navController.navigate(Rutas.BIENVENIDA) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
                onNavegar = { pestana -> navController.navegarAPestana(pestana) }
            )
        }

        composable(
            route = Rutas.DETALLE,
            arguments = listOf(navArgument("productoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getInt("productoId") ?: 0
            val producto = listaProductosFake.first { it.id == productoId }

            DetalleProductoScreen(
                producto = producto,
                esFavorito = producto.id in favoritos,
                onVolver = { navController.popBackStack() },
                onFavoritoClick = { alternarFavorito(producto) },
                onAgregarAlCarrito = { productoSeleccionado, cantidad ->
                    carrito = agregarOSumarProducto(carrito, productoSeleccionado, cantidad)
                    navController.popBackStack()
                }
            )
        }

        composable(Rutas.FAVORITOS) {
            FavoritosScreen(
                // Se arma la lista a partir de los ids guardados, en el orden del catálogo
                productos = listaProductosFake.filter { it.id in favoritos },
                onVolver = { navController.popBackStack() },
                onProductoClick = { producto ->
                    navController.navigate(Rutas.detalle(producto.id))
                },
                onAgregarProducto = { producto ->
                    carrito = agregarOSumarProducto(carrito, producto, 1)
                },
                // Todos están marcados, así que tocar el corazón los quita de la lista
                onQuitarFavorito = alternarFavorito
            )
        }

        composable(Rutas.CARRITO) {
            CarritoScreen(
                carrito = carrito,
                tipoEntrega = tipoEntrega,
                onVolver = { navController.popBackStack() },
                onIncrementar = { producto ->
                    carrito = carrito.map {
                        if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad + 1) else it
                    }
                },
                onDecrementar = { producto ->
                    carrito = carrito.mapNotNull {
                        when {
                            it.producto.id != producto.id -> it
                            it.cantidad > 1 -> it.copy(cantidad = it.cantidad - 1)
                            else -> null // si llega a 0, se elimina de la lista
                        }
                    }
                },
                onEliminar = { producto ->
                    carrito = carrito.filterNot { it.producto.id == producto.id }
                },
                onContinuarPedido = { navController.navigate(Rutas.ENTREGA) }
            )
        }

        composable(Rutas.ENTREGA) {
            // Los montos se calculan aquí a partir del carrito, con la misma fórmula
            // que CarritoScreen. DatosEntregaScreen solo los recibe y los muestra.
            // Como tipoEntrega es estado, al marcar otro RadioButton este bloque se
            // vuelve a ejecutar y el total se recalcula solo (S/ 4.00 o S/ 0.00 de envío).
            val subtotal = carrito.sumOf { it.producto.precio * it.cantidad }
            val total = subtotal + tipoEntrega.costo

            DatosEntregaScreen(
                subtotal = subtotal,
                tipoEntrega = tipoEntrega,
                total = total,
                onTipoEntregaCambia = { tipoEntrega = it },
                onVolver = { navController.popBackStack() },
                onConfirmarPedido = { nombre, telefono, direccion, referencia ->
                    // Por ahora los pedidos viven en memoria, en la lista "pedidos"
                    // de ClienteApp: no hay un servidor a donde enviarlos, así que
                    // se pierden al cerrar la app. El número es correlativo.
                    pedidos.add(
                        Pedido(
                            numero = pedidos.size + 1,
                            productos = carrito,
                            total = total,
                            tipoEntrega = tipoEntrega,
                            direccion = direccion
                        )
                    )

                    // Pila antes:   Inicio → Carrito → Entrega
                    // Pila después: Inicio → Confirmación
                    // popUpTo(INICIO) saca todo lo que está ENCIMA de Inicio (Carrito y
                    // Entrega) antes de abrir Confirmación. Así, al presionar "atrás" en
                    // Confirmación se vuelve a Inicio y no a un carrito ya pagado.
                    navController.navigate(Rutas.CONFIRMACION) {
                        popUpTo(Rutas.INICIO)
                    }

                    // El pedido ya se hizo: se vacía el carrito. Como el carrito vive
                    // aquí arriba, Inicio actualiza solo el contador del badge a 0.
                    carrito = emptyList()
                }
            )
        }

        composable(Rutas.CONFIRMACION) {
            ConfirmacionScreen(
                // Sigue siendo la opción con la que se confirmó el pedido
                tipoEntrega = tipoEntrega,
                onVolverAlInicio = {
                    // Pila antes:   Inicio → Confirmación
                    // Pila después: Inicio
                    // popUpTo(INICIO) saca Confirmación de la pila, y launchSingleTop
                    // reutiliza el Inicio que ya estaba abajo en vez de crear otro
                    // encima (si no, "atrás" en Inicio mostraría otro Inicio igual).
                    navController.navigate(Rutas.INICIO) {
                        popUpTo(Rutas.INICIO)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

/**
 * Navegación de la barra inferior (Inicio, Categorías, Pedidos y Perfil).
 * Es una función de extensión de NavHostController para escribir solo
 * navController.navegarAPestana(pestana) desde cada pantalla con barra.
 *
 * Las 3 opciones evitan que las pestañas se apilen una sobre otra:
 * - popUpTo(INICIO) { saveState = true }: antes de abrir la pestaña nueva,
 *   saca de la pila todo lo que está encima de Inicio, pero GUARDA su estado
 *   (por ejemplo, hasta dónde bajó el usuario en la lista).
 * - launchSingleTop = true: si ya se está en esa pestaña, no la abre otra vez.
 * - restoreState = true: si esa pestaña se visitó antes, recupera su estado guardado.
 * Así la pila siempre queda Inicio → (pestaña actual), y "atrás" desde
 * cualquier pestaña vuelve a Inicio.
 */
private fun NavHostController.navegarAPestana(pestana: PestanaNavegacion) {
    // Cada pestaña del enum se traduce a su ruta del NavHost
    val ruta = when (pestana) {
        PestanaNavegacion.INICIO -> Rutas.INICIO
        PestanaNavegacion.CATEGORIAS -> Rutas.CATEGORIAS
        PestanaNavegacion.PEDIDOS -> Rutas.PEDIDOS
        PestanaNavegacion.PERFIL -> Rutas.PERFIL
    }
    navigate(ruta) {
        popUpTo(Rutas.INICIO) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

/**
 * Si el producto ya está en el carrito, le suma la cantidad;
 * si no, lo agrega como un ItemCarrito nuevo.
 */
private fun agregarOSumarProducto(
    carrito: List<ItemCarrito>,
    producto: Producto,
    cantidad: Int
): List<ItemCarrito> {
    val itemExistente = carrito.find { it.producto.id == producto.id }
    return if (itemExistente != null) {
        carrito.map {
            if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad + cantidad) else it
        }
    } else {
        carrito + ItemCarrito(producto = producto, cantidad = cantidad)
    }
}