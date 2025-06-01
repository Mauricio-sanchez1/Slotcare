package com.example.slotcare0011


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.slotcare0011.ui.theme.SlotCare0011Theme
import components.BarraNavInferior
import components.bottomNavItems
import navigation.AppNavGraph


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SlotCare0011Theme {
                MainAppScreen()
            }
        }
    }
}

/**
 * Composable raíz de la aplicación.
 * Configura el NavController y Scaffold (para la barra inferior),
 * y delega la definición del grafo de navegación a AppNavGraph.
 */
@Composable
fun MainAppScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Usa el bottomNavItems importado
    val showBottomBarOnRoutes = bottomNavItems.map { it.route }.toSet()
    val shouldShowBottomBar = currentRoute in showBottomBarOnRoutes

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                // BarraNavInferior también usará el bottomNavItems importado
                // (directa o indirectamente si se lo pasas como parámetro)
                BarraNavInferior(navController = navController /* ... */)
            }
        }
    ) { innerPadding ->
        AppNavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}


