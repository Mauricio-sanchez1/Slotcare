package screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import components.BarraSuperior
import components.BotonServicios
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun PantallaDeServiciosConDrawer(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    Box(modifier = Modifier.fillMaxSize()) {
//        Image(
//            painter = painterResource(id = R.drawable.fondo_servicios),
//            contentDescription = "Fondo de pantalla de servicios",
//            modifier = Modifier.fillMaxSize(),
//            contentScale = ContentScale.Crop
//        )
        Scaffold(
            topBar = {
                BarraSuperior(
                    navController = navController,
                    onMenuClick = { // Nueva lambda para el clic del menú
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                )
            },
            containerColor = Color.Transparent,
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BotonServicios(
                    onRepararPcClick = { navController.navigate("reparar_pc_screen") },
                    // 👇 AÑADE LA ACCIÓN PARA onNuevaOrdenClick AQUÍ 👇
                    onNuevaOrdenClick = { navController.navigate("nueva_orden_screen") },
                    // Asumiendo que también tienes estos parámetros en BotonServicios:
                    onRepararCpuClick = { navController.navigate("reparar_cpu_screen") },
                    onAddDispositivoClick = { navController.navigate("add_dispositivo_screen") },
                    onHistorialClick = { navController.navigate("historial_screen") },
                    onConsultaTecnicaClick = { navController.navigate("consulta_tecnica_screen") },

                    )
            }
        }
    }
}

