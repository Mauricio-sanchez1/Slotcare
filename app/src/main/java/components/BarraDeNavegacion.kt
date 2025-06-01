package components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ManageSearch
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ManageSearch
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.slotcare0011.ui.theme.SlotCare0011Theme
import kotlin.collections.forEach
import kotlin.collections.listOf


sealed class Screen(val route: String, val label: String, val iconFilled: ImageVector, val iconOutlined: ImageVector) {
    object Home : Screen("home_screen", "Inicio",
        Icons.Filled.Home, Icons.Outlined.Home)
    object Activities : Screen("activities_screen", "Actividades",
        Icons.Filled.Route, Icons.Outlined.Route)
    object Explore : Screen("explore_screen", "Explorar",
        Icons.Filled.ManageSearch, Icons.Outlined.ManageSearch)
    object Settings : Screen("settings_screen", "Ajustes",
        Icons.Filled.Settings, Icons.Outlined.Settings)
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Activities,
    Screen.Explore,
    Screen.Settings
)

fun Modifier.topShadow(
    color: Color = Color.Black,
    alpha: Float = 0.05f, // Sombra muy sutil
    height: Dp = 4.dp // Altura de la sombra (el área del gradiente)
): Modifier = this.then(
    drawBehind {
        val shadowColor = color.copy(alpha = alpha).toArgb() // Convertir a ARGB int para compatibilidad
        val transparent = color.copy(alpha = 0f).toArgb()  // Convertir a ARGB int

        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(shadowColor), Color(transparent)),
                startY = 0f,
                endY = height.toPx()
            ),
            size = this.size.copy(height = height.toPx())
        )
    }
)


@Composable
fun BarraNavInferior(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        modifier = modifier
            .topShadow(alpha = 0.1f,
                height = 6.dp), // <--- APLICAMOS LA SOMBRA SUPERIOR AQUÍ
        containerColor = Color.White,
        contentColor = Color.Gray,
        tonalElevation = 0.dp
    ) {
        bottomNavItems.forEach { screen ->
            val selected = currentRoute == screen.route
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (selected) screen.iconFilled else screen.iconOutlined,
                        contentDescription = screen.label
                    )
                },
                label = { Text(screen.label) },
                selected = selected,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BarraNavInferiorPreview() {
    val navController = rememberNavController()
    SlotCare0011Theme {
        // Para ver mejor la sombra en el preview, podemos poner un fondo
        // Box(modifier = Modifier.fillMaxSize().background(Color.LightGray.copy(alpha=0.3f))) {
        BarraNavInferior(navController = navController)
        // }
    }
}