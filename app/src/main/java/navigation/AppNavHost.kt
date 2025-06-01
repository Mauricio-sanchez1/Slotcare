package navigation


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import components.BotonServicios
import components.Screen
import screens.BienvenidaScreen
import screens.FormularioNuevaOrden
import screens.FormularioNuevoDispositivo
import screens.NuevoTecnico
import screens.OrderDetailScreen
import screens.PantallaInicio
import screens.SlotCareLoginScreen


object Destinations {
    const val BIENVENIDA = "bienvenida"
    const val LOGIN = "login"

    val HOME = Screen.Home.route
    val ACTIVITIES = Screen.Activities.route
    val EXPLORE = Screen.Explore.route
    val SETTINGS = Screen.Settings.route

    const val NUEVA_ORDEN = "NuevaOrden"
    const val NUEVO_TECNICO = "NuevoTecnico"
    const val FORMULARIO_NUEVO_DISPOSITIVO = "FormularioNuevoDispositivo"
    const val REPARAR_CPU = "reparar_cpu_screen"
    const val HISTORIAL = "historial_screen"
    const val CONSULTA_TECNICA = "consulta_tecnica_screen"
    const val CREAR_ALGO_NUEVO = "crear_algo_nuevo_screen"

    const val ORDER_DETAIL_BASE = "order_detail"
    const val ORDER_ID_ARG = "orderId"
    const val ORDER_DETAIL_WITH_ARG = "$ORDER_DETAIL_BASE/{$ORDER_ID_ARG}"
}

// --- Composable Principal del Grafo de Navegación ---
@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier // Recibe el modifier con el padding del Scaffold desde MainActivity
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.BIENVENIDA,
        modifier = modifier // Aplicar el modifier aquí para respetar el padding del Scaffold
    ) {
        // --- PANTALLA DE BIENVENIDA ---
        composable(route = Destinations.BIENVENIDA) {
            BienvenidaScreen( // Asegúrate de que este Composable esté definido/importado
                onLogin = { navController.navigate(Destinations.LOGIN) },
                onNavigateToServicios = {
                    navController.navigate(Destinations.HOME) {
                        popUpTo(Destinations.BIENVENIDA) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // --- PANTALLA DE LOGIN ---
        composable(route = Destinations.LOGIN) {
            SlotCareLoginScreen( // Asegúrate de que este Composable esté definido/importado
                onBack = { navController.popBackStack() },
                onLoginSuccess = {
                    navController.navigate(Destinations.HOME) {
                        popUpTo(Destinations.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // --- PANTALLAS PRINCIPALES (Bottom Nav) ---
        composable(route = Destinations.HOME) {
            PantallaInicio(navController = navController)
        }
        composable(route = Destinations.ACTIVITIES) {
            Box(modifier = Modifier.fillMaxSize()) { // Ejemplo de contenido, reemplaza con tu pantalla real
                BotonServicios(
                    onRepararPcClick = { navController.navigate(Destinations.NUEVO_TECNICO) },
                    onNuevaOrdenClick = { navController.navigate(Destinations.NUEVA_ORDEN) },
                    onRepararCpuClick = { navController.navigate(Destinations.REPARAR_CPU) },
                    onAddDispositivoClick = { navController.navigate(Destinations.FORMULARIO_NUEVO_DISPOSITIVO) },
                    onHistorialClick = { navController.navigate(Destinations.HISTORIAL) },
                    onConsultaTecnicaClick = { navController.navigate(Destinations.CONSULTA_TECNICA) },
                )
            }
        }
        composable(Destinations.EXPLORE) { Text("Pantalla Explorar (Contenido Pendiente)") }
        composable(Destinations.SETTINGS) { Text("Pantalla Ajustes (Contenido Pendiente)") }

        // --- PANTALLAS SECUNDARIAS ---
        composable(Destinations.NUEVA_ORDEN) {
            FormularioNuevaOrden(onBack = { navController.popBackStack() })
        }
        composable(Destinations.NUEVO_TECNICO) {
            NuevoTecnico(
                onBack = { navController.popBackStack() },
                onSaveSuccessNavigation = {
                    navController.navigate(Destinations.HOME) {
                        popUpTo(Destinations.NUEVO_TECNICO) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Destinations.FORMULARIO_NUEVO_DISPOSITIVO) {
            FormularioNuevoDispositivo(
                onBack = { navController.popBackStack() },
                onSaveSuccessNavigation = {
                    navController.navigate(Destinations.HOME) {
                        popUpTo(Destinations.FORMULARIO_NUEVO_DISPOSITIVO) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Destinations.REPARAR_CPU) { Text("Pantalla Reparar CPU") }
        composable(Destinations.HISTORIAL) { Text("Pantalla Historial") }
        composable(Destinations.CONSULTA_TECNICA) { Text("Pantalla Consulta Técnica") }
        composable(Destinations.CREAR_ALGO_NUEVO) { Text("Pantalla Crear Algo Nuevo (FAB)") }


        // --- NUEVO: RUTA PARA DETALLES DE LA ORDEN ---
        composable(
            route = Destinations.ORDER_DETAIL_WITH_ARG,
            arguments = listOf(navArgument(Destinations.ORDER_ID_ARG) { type = NavType.StringType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString(Destinations.ORDER_ID_ARG)
            if (orderId != null) {
                OrderDetailScreen(
                    orderId = orderId,
                    navController = navController
                ) // DESCOMENTADO Y USADO
            } else {
                Text("Error: ID de orden no encontrado.")
            }
        }
    }
}