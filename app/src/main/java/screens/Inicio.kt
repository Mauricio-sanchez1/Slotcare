package screens

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import components.AccesosDirectosCard
import navigation.Destinations
import components.OrderCard
import viewmodels.OrderViewModel
import viewmodels.PreviewOrderViewModel
import com.example.slotcare0011.R
import com.example.slotcare0011.ui.theme.SlotCare0011Theme


// --- DATA CLASSES Y COMPOSABLES AUXILIARES (A NIVEL SUPERIOR O EN OTROS ARCHIVOS) ---

data class UserInfo(
    val initials: String = "SC",
    val firstName: String = "Slot",
    val lastName: String = "Care"
)

// ESTA ES LA ÚNICA DEFINICIÓN DE InfoAcceso QUE DEBE EXISTIR EN TU PROYECTO
// (a menos que decidas moverla a AccesosDirectos.kt y eliminarla de aquí)
data class InfoAcceso(
    val titulo: String,
    @DrawableRes val imagenResId: Int,
    val accion: () -> Unit // El nombre de la lambda es 'accion'
)

@Composable
fun UserAvatar(
    initials: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    size: Dp = 40.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials.uppercase(),
            color = contentColor,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

// --- PANTALLA PRINCIPAL ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicio(
    navController: NavController,
    userInfo: UserInfo = UserInfo(), // Asegúrate de que UserInfo esté definido
    orderViewModel: OrderViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf("") }

    // --- PUNTO CRÍTICO CORREGIDO: LaunchedEffect ---
    LaunchedEffect(key1 = orderViewModel) {
        Log.d("PantallaInicio", "LaunchedEffect: Llamando a orderViewModel.cargarListaDeOrdenesFirestore()")
        orderViewModel.cargarListaDeOrdenesFirestore()
    } // <--- ESTA ES LA LLAVE DE CIERRE QUE FALTABA PARA LaunchedEffect

    val ordenes by orderViewModel.listaDeOrdenes.collectAsState()
    val isLoading by orderViewModel.isLoadingLista.collectAsState() // Para mostrar un indicador de carga
    val error by orderViewModel.errorLista.collectAsState() // Para mostrar mensajes de error

    val listaDeAccesos: List<InfoAcceso> = remember {
        listOf(
            InfoAcceso(
                titulo = "Orden",
                imagenResId = R.drawable.add_10322002, // Reemplaza con tu ícono real
                accion = {
                    Log.d("PantallaInicio", "Acceso directo 'Orden' clickeado")
                    navController.navigate(Destinations.NUEVA_ORDEN) // Ejemplo de navegación
                }
            ),
            InfoAcceso(
                titulo = "Dispositivo",
                imagenResId = R.drawable.add_dispositivo, // Reemplaza con tu ícono real
                accion = {
                    Log.d("PantallaInicio", "Acceso directo 'Dispositivo' clickeado")
                    navController.navigate(Destinations.FORMULARIO_NUEVO_DISPOSITIVO) // Ejemplo
                }
            ),
            InfoAcceso(
                titulo = "Tecnico",
                imagenResId = R.drawable.add_operario, // Reemplaza con tu ícono real
                accion = {
                    Log.d("PantallaInicio", "Acceso directo 'Tecnico' clickeado")
                    navController.navigate(Destinations.NUEVO_TECNICO) // Ejemplo
                }
            )
        )
    }

    Scaffold(
        topBar = {
            // TopAppBar(title = { Text("Slot Care") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(vertical = 8.dp)
        ) {
            // Sección de Saludo del Usuario
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp, top = 4.dp)
            ) {
                UserAvatar(initials = userInfo.initials)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Hola, ${userInfo.firstName} ${userInfo.lastName}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Sección de Búsqueda y Escáner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Buscar orden, dispositivo...") },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = "Icono de búsqueda"
                        )
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = CircleShape,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = { println("Lector QR Click") },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                    // Lógica para el escáner QR, ej: navController.navigate("qr_scanner_screen")
                ) {
                    Icon(
                        Icons.Filled.QrCodeScanner,
                        contentDescription = "Lector de código de barras",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            // --- SECCIÓN DE ACCESOS DIRECTOS ---
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // ASEGÚRATE DE ESTA ESTRUCTURA
                items( // <--- Esta es la línea que origina el error
                    listaDeAccesos,
                    key = { acceso -> acceso.titulo }
                ) { infoAccesoItem ->
                    AccesosDirectosCard(info = infoAccesoItem)
                }
            }
            // --- FIN DE SECCIÓN DE ACCESOS DIRECTOS ---

            Spacer(modifier = Modifier.height(24.dp))
            // --- NUEVO: SECCIÓN DE LISTA DE ÓRDENES DE TRABAJO ---
            Text(
                text = "Órdenes de Trabajo Recientes",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                        Log.d("PantallaInicio", "Mostrando CircularProgressIndicator (isLoading es true)")
                    }
                }
                error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Error al cargar órdenes: $error",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                        Log.e("PantallaInicio", "Mostrando mensaje de error: $error")
                    }
                }
                else -> {
                    // Filtrar órdenes si hay una query de búsqueda
                    val ordenesFiltradas = if (searchQuery.isNotBlank()) {
                        ordenes.filter { orden ->
                            // Lógica de filtrado (ajusta según tus campos en OrdenDeTrabajo)
                            orden.numeroOrden.contains(searchQuery, ignoreCase = true) ||
                                    (orden.asignadoA?.contains(searchQuery, ignoreCase = true) == true) ||
                                    orden.ubicacion.contains(searchQuery, ignoreCase = true) ||
                                    orden.tipoDispositivo.contains(searchQuery, ignoreCase = true) ||
                                    orden.numeroSerie.contains(searchQuery, ignoreCase = true)
                            // Añade más campos si es necesario
                        }
                    } else {
                        ordenes
                    }

                    if (ordenesFiltradas.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                if (searchQuery.isNotBlank()) "No se encontraron órdenes para \"$searchQuery\"."
                                else "No hay órdenes de trabajo activas."
                            )
                            Log.d("PantallaInicio", if (searchQuery.isNotBlank()) "Mostrando 'No se encontraron órdenes para $searchQuery'" else "Mostrando 'No hay órdenes de trabajo activas'")
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 4.dp),
                            contentPadding = PaddingValues(bottom = 8.dp)
                        ) {
                            items(ordenesFiltradas, key = { it.numeroOrden }) { orden ->
                                OrderCard( // Asume que OrderCard está definido
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    numeroOrden = orden.numeroOrden,
                                    asignadoA = orden.asignadoA,
                                    ubicacion = orden.ubicacion,
                                    estadoString = orden.estado,
                                    onClick = { orderId ->
                                        Log.d(
                                            "PantallaInicio",
                                            "OrderCard clickeada, navegando a detalle: $orderId"
                                        )
                                        navController.navigate("${Destinations.ORDER_DETAIL_BASE}/$orderId")
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- PREVIEWS ---
@Preview(showBackground = true, name = "Pantalla de Inicio Completa")
@Composable
fun PantallaInicioPreviewCompleta() {
    SlotCare0011Theme {
        val navController = rememberNavController()

        // --- LA LÍNEA CRÍTICA MODIFICADA ---
        // En lugar de: val previewViewModel: OrderViewModel = viewModel()
        // Usa esto:
        val previewViewModel: OrderViewModel = PreviewOrderViewModel() // <--- ¡CAMBIO IMPORTANTE!

        PantallaInicio(
            navController = navController,
            userInfo = UserInfo(initials = "JD", firstName = "John", lastName = "Doe"),
            orderViewModel = previewViewModel // Ahora previewViewModel es una instancia de PreviewOrderViewModel
        )
    }
}