package components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.slotcare0011.ui.theme.SlotCare0011Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior(
    navController: NavController,
    onMenuClick: () -> Unit // <<--- TIPO CORREGIDO AQUÍ
) {
    TopAppBar(
        title = {
            Box (
                modifier = Modifier.fillMaxWidth(), // Mejor usar fillMaxWidth para el título
                contentAlignment = Alignment.Center
            ){
                Text(
                    "Slot Care",
                    // El padding aquí es opcional si el Box ya centra bien
                    // modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.titleLarge, // Buen añadido para el estilo
                    textAlign = TextAlign.Center,
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) { // Ya no necesita cast
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu Principal",
                )
            }
        },
        actions = {
            IconButton(onClick = {/*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Busqueda",
                )
            }
            IconButton(onClick = {/*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Compartir",
                )
            }
            IconButton(onClick = { navController.navigate("login") }) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Iniciar Sesion",
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}


@Preview(showBackground = true)
@Composable
fun BotoBarPreview() {
    SlotCare0011Theme {
        val navController = rememberNavController()
        BarraSuperior(
            navController = navController,
            onMenuClick = {
                // Para la preview, una acción simple o ninguna acción es suficiente.
                // Esto hace que la preview compile y sea funcional para ver el componente.
                println("Preview: Botón de menú clickeado!")
            }
        )
    }
}
//                                Scaffold(
//                                    topBar = {
//                                        BarraSuperior(
//                                            navController,
//                                            { //  lambda para el clic del menú
//                                                scope.launch {
//                                                    drawerState.apply {
//                                                        if (isClosed) open() else close()
//                                                    }
//                                                }
//                                            }
//                                        )
//                                    },
//                                    //  el Scaffold transparente para que se vea la imagen de fondo
//                                    containerColor = Color.Transparent, // ¡Importante!
//                                    contentColor = MaterialTheme.colorScheme.onSurface // Color para el contenido del Scaffold si no se especifica otro
//                                ) { paddingValues ->
//                                    Column(
//                                        modifier = Modifier
//                                            .fillMaxSize()
//                                            .padding(paddingValues),
//                                        horizontalAlignment = Alignment.CenterHorizontally
//                                    ) {
//ModalNavigationDrawer(
//drawerState = drawerState,
//drawerContent = {
//    ModalDrawerSheet {
//        Spacer(Modifier.height(16.dp))
//        NavigationDrawerItem(
//            icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
//            label = { Text("Inicio") },
//            selected = navController.currentDestination?.route == "home",
//            onClick = {
//                scope.launch { drawerState.close() }
//                navController.navigate("home") { launchSingleTop = true }
//            },
//            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
//        )
//        NavigationDrawerItem(
//            icon = {
//                Icon(
//                    Icons.Filled.AccountCircle,
//                    contentDescription = "Iniciar Sesión"
//                )
//            },
//            label = { Text("Iniciar Sesión") },
//            selected = navController.currentDestination?.route == "login",
//            onClick = {
//                scope.launch { drawerState.close() }
//                navController.navigate("login") { launchSingleTop = true }
//            },
//            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
//        )
//
//        NavigationDrawerItem(
//            icon = {
//                Icon(
//                    Icons.Filled.Info,
//                    contentDescription = "Acerca de"
//                )
//            },
//            label = { Text("acerca de") },
//            selected = navController.currentDestination?.route == "acerca_de",
//            onClick = {
//                scope.launch { drawerState.close() }
//                navController.navigate("acerca_de") { launchSingleTop = true }
//            },
//            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
//        )
//        NavigationDrawerItem(
//            icon = {
//                Icon(
//                    Icons.Filled.Settings,
//                    contentDescription = "Configuracion"
//                )
//            },
//            label = { Text("Configuracion") },
//            selected = navController.currentDestination?.route == "configuracion",
//            onClick = {
//                scope.launch { drawerState.close() }
//                navController.navigate("configuracion") {
//                    launchSingleTop = true
//                }
//            },
//            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
//        )
//    }
//}
//) {