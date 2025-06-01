package screens // O tu paquete

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import components.EstadoOrden
import viewmodels.OrderViewModel
import com.example.slotcare0011.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    orderId: String,
    navController: NavController,
    orderViewModel: OrderViewModel = viewModel()
) {
    var showDialogTecnico by remember { mutableStateOf(false) }
    var showDialogEstado by remember { mutableStateOf(false) }


    LaunchedEffect(key1 = orderId) {
        Log.d("OrderDetailScreen", "Cargando detalle para orden ID: $orderId")
        orderViewModel.cargarDetalleOrden(orderId)
        orderViewModel.cargarListaDeTecnicos() // Cargar lista de técnicos
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("OrderDetailScreen", "Limpiando detalle de orden.")
            orderViewModel.limpiarDetalleOrden()
        }
    }

    val orden by orderViewModel.selectedOrder.collectAsState()
    val isLoading by orderViewModel.isLoadingDetail.collectAsState()
    val error by orderViewModel.errorDetail.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(orden?.numeroOrden ?: "Detalle de Orden") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        },
        floatingActionButton = {
            orden?.let { currentOrder ->
                // Solo mostrar FAB si la orden no está finalizada
                if (EstadoOrden.Companion.fromString(currentOrder.estado) != EstadoOrden.FINALIZADA) {
                    ExtendedFloatingActionButton(
                        onClick = {
                            Log.d("OrderDetailScreen", "Botón Finalizar Orden presionado para ${currentOrder.numeroOrden}")
                            orderViewModel.finalizarOrden(currentOrder.numeroOrden)
                        },
                        icon = { Icon(painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "Finalizar") }, // Reemplaza con tu ícono
                        text = { Text("Finalizar Orden") }
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // Para que el contenido sea scrollable
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter // Cambiado para alinear el contenido arriba
        ) {
            when {
                isLoading && orden == null -> { // Mostrar carga solo si no hay datos previos
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    Log.d("OrderDetailScreen", "Mostrando CircularProgressIndicator (orden es null)")
                }
                error != null -> {
                    Text("Error: $error", color = MaterialTheme.colorScheme.error, modifier = Modifier.align(Alignment.Center))
                    Log.e("OrderDetailScreen", "Mostrando error: $error")
                }
                orden != null -> {
                    val currentOrden = orden!! // Sabemos que no es null aquí
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DetailItem(label = "No. Trabajo:", value = currentOrden.numeroOrden)
                        DetailItem(label = "Ubicación:", value = currentOrden.ubicacion)
                        DetailItem(label = "Tipo Dispositivo:", value = currentOrden.tipoDispositivo)
                        DetailItem(label = "No. Serie:", value = currentOrden.numeroSerie)
                        DetailItem(
                            label = "Descripción Problema:",
                            value = currentOrden.descripcionProblema,
                            isFullWidthValue = true
                        )

                        EditableDetailItem(
                            label = "Asignado a:",
                            value = currentOrden.asignadoA ?: "No asignado",
                            onEditClick = {
                                Log.d("OrderDetailScreen", "Editando técnico")
                                showDialogTecnico = true
                            }
                        )

                        val estadoActual = EstadoOrden.Companion.fromString(currentOrden.estado)
                        EditableDetailItem(
                            label = "Estado:",
                            value = estadoActual.displayName,
                            valueColor = estadoActual.color,
                            onEditClick = {
                                Log.d("OrderDetailScreen", "Editando estado")
                                if (estadoActual != EstadoOrden.FINALIZADA) { // No permitir editar si ya está finalizada
                                    showDialogEstado = true
                                }
                            },
                            showEditIcon = estadoActual != EstadoOrden.FINALIZADA // Ocultar icono si está finalizada
                        )

                        DetailItem(
                            label = "Fecha Creación:",
                            value = convertMillisToDateString(currentOrden.fechaCreacion)
                        )

                        currentOrden.imageUrl?.let { url ->
                            Text("Imagen Adjunta:", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp))
                            AsyncImage(
                                model = url,
                                contentDescription = "Imagen de la orden",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant),
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(id = R.drawable.consulta), // Reemplaza
                                error = painterResource(id = R.drawable.agregar_tecnico) // Reemplaza
                            )
                        }

                        // Indicador de carga sutil si estamos actualizando algo
                        if (isLoading && orden != null) {
                            Spacer(Modifier.height(16.dp))
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                            Log.d("OrderDetailScreen", "Mostrando CircularProgressIndicator (actualizando datos)")
                        }
                    }
                }
                else -> {
                    Text("No hay datos de la orden disponibles.", modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }

    // --- DIÁLOGOS ---
    if (showDialogTecnico && orden != null) {
        val tecnicos by orderViewModel.listaTecnicos.collectAsState()
        SingleChoiceDialog(
            title = "Seleccionar Técnico",
            options = tecnicos,
            selectedOption = orden!!.asignadoA ?: "Sin asignar",
            onDismiss = { showDialogTecnico = false },
            onConfirm = { nuevoTecnico ->
                orderViewModel.actualizarTecnicoDeOrden(orderId, nuevoTecnico)
                showDialogTecnico = false
            }
        )
    }

    if (showDialogEstado && orden != null) {
        val estadoActual = EstadoOrden.Companion.fromString(orden!!.estado)
        SingleChoiceDialog(
            title = "Seleccionar Estado",
            options = orderViewModel.listaEstadosPosibles.map { it.displayName },
            selectedOption = estadoActual.displayName,
            onDismiss = { showDialogEstado = false },
            onConfirm = { nuevoEstadoString ->
                val nuevoEstadoEnum = EstadoOrden.Companion.fromString(nuevoEstadoString)
                if (nuevoEstadoEnum != EstadoOrden.DESCONOCIDO) {
                    orderViewModel.actualizarEstadoDeOrden(orderId, nuevoEstadoEnum)
                }
                showDialogEstado = false
            }
        )
    }
}

@Composable
fun DetailItem(label: String, value: String, isFullWidthValue: Boolean = false) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Text(
            value,
            style = MaterialTheme.typography.bodyLarge,
            modifier = if (isFullWidthValue) Modifier.fillMaxWidth() else Modifier
        )
        Divider(modifier = Modifier.padding(top = 6.dp, bottom = 6.dp))
    }
}

@Composable
fun EditableDetailItem(
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
    onEditClick: () -> Unit,
    showEditIcon: Boolean = true
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            if (showEditIcon) {
                IconButton(onClick = onEditClick, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Filled.Edit, contentDescription = "Editar $label", tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
        Text(value, style = MaterialTheme.typography.bodyLarge, color = valueColor)
        Divider(modifier = Modifier.padding(top = 6.dp, bottom = 6.dp))
    }
}

@Composable
fun SingleChoiceDialog(
    title: String,
    options: List<String>,
    selectedOption: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var currentSelection by remember(selectedOption) { mutableStateOf(selectedOption) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(title, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(bottom = 16.dp))
                options.forEach { option ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { currentSelection = option }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (option == currentSelection),
                            onClick = { currentSelection = option }
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(option, style = MaterialTheme.typography.bodyLarge)
                    }
                }
                Spacer(Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = { onConfirm(currentSelection) }) {
                        Text("Aceptar")
                    }
                }
            }
        }
    }
}

fun convertMillisToDateString(millis: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(millis))
}

class PreviewOrderDetailViewModel : OrderViewModel() {
    private val _previewSelectedOrder = MutableStateFlow<OrdenDeTrabajo?>(null)
    override val selectedOrder: StateFlow<OrdenDeTrabajo?> = _previewSelectedOrder.asStateFlow()

    private val _previewIsLoadingDetail = MutableStateFlow(false)
    override val isLoadingDetail: StateFlow<Boolean> = _previewIsLoadingDetail.asStateFlow()

    private val _previewErrorDetail = MutableStateFlow<String?>(null)
    override val errorDetail: StateFlow<String?> = _previewErrorDetail.asStateFlow()

    private val _previewListaTecnicos = MutableStateFlow<List<String>>(emptyList())
    override val listaTecnicos: StateFlow<List<String>> = _previewListaTecnicos.asStateFlow()

    override val listaEstadosPosibles: List<EstadoOrden> = EstadoOrden.entries.toList()


    init {
        cargarDetalleOrden("PREVIEW_ID_ACTIVA")
        cargarListaDeTecnicos()
    }

    override fun cargarDetalleOrden(orderId: String) {
        viewModelScope.launch {
            _previewIsLoadingDetail.value = true
            delay(300)
            when (orderId) {
                "PREVIEW_ID_ACTIVA" -> {
                    _previewSelectedOrder.value = OrdenDeTrabajo(
                        numeroOrden = "OT-PREVIEW-001",
                        asignadoA = "Técnico Preview Alfa",
                        ubicacion = "Sala de Juntas A",
                        tipoDispositivo = "Proyector XL-500",
                        numeroSerie = "SN789123",
                        descripcionProblema = "El proyector no enciende y muestra una luz roja parpadeante. Se intentó reiniciar varias veces sin éxito.",
                        estado = EstadoOrden.ACTIVA.displayName,
                        fechaCreacion = System.currentTimeMillis() - 86400000,
                        imageUrl = "https://via.placeholder.com/300/CCCCCC/FFFFFF?text=Imagen+Orden"
                    )
                    _previewErrorDetail.value = null
                }
                "PREVIEW_ID_FINALIZADA" -> {
                    _previewSelectedOrder.value = OrdenDeTrabajo(
                        numeroOrden = "OT-PREVIEW-002",
                        asignadoA = "Técnico Preview Beta",
                        ubicacion = "Oficina Principal",
                        tipoDispositivo = "Impresora Multifunción",
                        numeroSerie = "SN123456",
                        descripcionProblema = "Atasco de papel constante.",
                        estado = EstadoOrden.FINALIZADA.displayName,
                        fechaCreacion = System.currentTimeMillis() - (86400000 * 2),
                        imageUrl = null
                    )
                    _previewErrorDetail.value = null
                }
                "PREVIEW_ID_ERROR" -> {
                    _previewSelectedOrder.value = null
                    _previewErrorDetail.value = "Error simulado al cargar la orden."
                }
                "PREVIEW_ID_LOADING" -> {
                    _previewIsLoadingDetail.value = true
                    _previewSelectedOrder.value = null
                    _previewErrorDetail.value = null
                    return@launch
                }
                else -> {
                    _previewSelectedOrder.value = null
                    _previewErrorDetail.value = "ID de orden no reconocido para preview."
                }
            }
            if (orderId != "PREVIEW_ID_LOADING") {
                _previewIsLoadingDetail.value = false
            }
        }
    }

    override fun cargarListaDeTecnicos() {
        viewModelScope.launch {
            delay(100)
            _previewListaTecnicos.value = listOf("Técnico Preview Alfa", "Técnico Preview Beta", "Técnico Gamma", "Sin asignar")
        }
    }

    override fun actualizarTecnicoDeOrden(orderId: String, nuevoTecnico: String?) {
        viewModelScope.launch {
            _previewIsLoadingDetail.value = true
            delay(300)
            val tecnicoParaGuardar = if (nuevoTecnico == "Sin asignar") null else nuevoTecnico
            _previewSelectedOrder.value = _previewSelectedOrder.value?.copy(asignadoA = tecnicoParaGuardar)
            _previewIsLoadingDetail.value = false
            Log.d("PreviewVM", "Técnico actualizado (preview) a: $tecnicoParaGuardar")
        }
    }

    override fun actualizarEstadoDeOrden(orderId: String, nuevoEstado: EstadoOrden) {
        viewModelScope.launch {
            _previewIsLoadingDetail.value = true
            delay(300)
            _previewSelectedOrder.value = _previewSelectedOrder.value?.copy(estado = nuevoEstado.displayName)
            _previewIsLoadingDetail.value = false
            Log.d("PreviewVM", "Estado actualizado (preview) a: ${nuevoEstado.displayName}")
        }
    }

    override fun finalizarOrden(orderId: String) {
        actualizarEstadoDeOrden(orderId, EstadoOrden.FINALIZADA)
    }

    override fun limpiarDetalleOrden() {
        _previewSelectedOrder.value = null
        _previewIsLoadingDetail.value = false
        _previewErrorDetail.value = null
    }
}

class OrderDetailPreviewParameterProvider : PreviewParameterProvider<String> {
    override val values = sequenceOf(
        "PREVIEW_ID_ACTIVA",
        "PREVIEW_ID_FINALIZADA",
        "PREVIEW_ID_ERROR",
        "PREVIEW_ID_LOADING_INIT"
    )
}

@Preview(name = "Detalle Orden Activa", showBackground = true, widthDp = 380, heightDp = 800)
@Composable
fun OrderDetailScreenPreviewActiva() {
    // TuTemaDeApp { // Descomenta si tienes un tema
    Surface(color = MaterialTheme.colorScheme.background) {
        OrderDetailScreen(
            orderId = "PREVIEW_ID_ACTIVA",
            navController = rememberNavController(),
            orderViewModel = PreviewOrderDetailViewModel()
        )
    }
    // }
}

@Preview(name = "Detalle Orden Finalizada", showBackground = true, widthDp = 380, heightDp = 800)
@Composable
fun OrderDetailScreenPreviewFinalizada() {
    // TuTemaDeApp {
    Surface(color = MaterialTheme.colorScheme.background) {
        val previewViewModel = PreviewOrderDetailViewModel()
        LaunchedEffect(Unit) { // Para asegurar que se carga el estado correcto para esta preview
            previewViewModel.cargarDetalleOrden("PREVIEW_ID_FINALIZADA")
        }
        OrderDetailScreen(
            orderId = "PREVIEW_ID_FINALIZADA",
            navController = rememberNavController(),
            orderViewModel = previewViewModel
        )
    }
    // }
}

@Preview(name = "Detalle Orden - Error", showBackground = true, widthDp = 380, heightDp = 800)
@Composable
fun OrderDetailScreenPreviewError() {
    // TuTemaDeApp {
    Surface(color = MaterialTheme.colorScheme.background) {
        val previewViewModel = PreviewOrderDetailViewModel()
        LaunchedEffect(Unit) {
            previewViewModel.cargarDetalleOrden("PREVIEW_ID_ERROR")
        }
        OrderDetailScreen(
            orderId = "PREVIEW_ID_ERROR",
            navController = rememberNavController(),
            orderViewModel = previewViewModel
        )
    }
    // }
}

@Preview(name = "Detalle Orden - Cargando", showBackground = true, widthDp = 380, heightDp = 800)
@Composable
fun OrderDetailScreenPreviewLoading() {
    // TuTemaDeApp {
    Surface(color = MaterialTheme.colorScheme.background) {
        val previewViewModel = PreviewOrderDetailViewModel()
        LaunchedEffect(Unit) {
            previewViewModel.cargarDetalleOrden("PREVIEW_ID_LOADING")
        }
        OrderDetailScreen(
            orderId = "PREVIEW_ID_LOADING",
            navController = rememberNavController(),
            orderViewModel = previewViewModel
        )
    }
    // }
}

@Preview(name = "OrderDetailScreen States (Iterativo)", showBackground = true, widthDp = 380, heightDp = 800)
@Composable
fun OrderDetailScreenPreviewWithProvider(
    @PreviewParameter(OrderDetailPreviewParameterProvider::class) orderIdParam: String
) {
    // TuTemaDeApp {
    Surface(color = MaterialTheme.colorScheme.background) {
        val previewViewModel = PreviewOrderDetailViewModel()
        if (orderIdParam == "PREVIEW_ID_LOADING_INIT") {
            OrderDetailScreen(
                orderId = "PREVIEW_ID_LOADING", // Dispara el estado de carga
                navController = rememberNavController(),
                orderViewModel = previewViewModel
            )
        } else {
            // El LaunchedEffect dentro de OrderDetailScreen llamará a cargarDetalleOrden con orderIdParam
            OrderDetailScreen(
                orderId = orderIdParam,
                navController = rememberNavController(),
                orderViewModel = previewViewModel
            )
        }
    }
}

