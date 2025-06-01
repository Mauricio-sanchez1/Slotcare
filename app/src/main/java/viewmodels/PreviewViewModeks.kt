package viewmodels

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import screens.OrdenDeTrabajo

class PreviewOrderViewModel : OrderViewModel() { // Asegúrate que OrderViewModel está importado y es 'open'

    private val _previewNumeroOrden = MutableStateFlow("PV-OT-PREVIEW-001")
    override val numeroOrden: StateFlow<String>
        get() = _previewNumeroOrden.asStateFlow()

    private val _previewSaveStatus = MutableStateFlow<OrderSaveEvent>(OrderSaveEvent.Idle)
    override val saveOrderStatus: StateFlow<OrderSaveEvent>
        get() = _previewSaveStatus.asStateFlow()

    // --- DATOS PARA LA LISTA DE ÓRDENES EN PantallaInicio ---
    private val _previewListaDeOrdenes = MutableStateFlow(
        listOf(
            OrdenDeTrabajo(
                numeroOrden = "PREVIEW-001",
                asignadoA = "Técnico PV",
                ubicacion = "Sala Demo",
                tipoDispositivo = "Laptop Demo",
                estado = "Activa",
                fechaCreacion = System.currentTimeMillis()
            ),
            OrdenDeTrabajo(
                numeroOrden = "PREVIEW-002",
                asignadoA = "Ana PV",
                ubicacion = "Oficina Test",
                tipoDispositivo = "Impresora Test",
                estado = "En Progreso",
                fechaCreacion = System.currentTimeMillis() - 100000L
            ),
            OrdenDeTrabajo(
                numeroOrden = "PREVIEW-003",
                asignadoA = "Carlos PV",
                ubicacion = "Almacén",
                tipoDispositivo = "Tablet Rota",
                estado = "Pendiente",
                fechaCreacion = System.currentTimeMillis() - 200000L
            )
        )
    )
    override val listaDeOrdenes: StateFlow<List<OrdenDeTrabajo>> // Debe ser 'open' en OrderViewModel
        get() = _previewListaDeOrdenes.asStateFlow()

    private val _previewIsLoadingLista = MutableStateFlow(false)
    override val isLoadingLista: StateFlow<Boolean> // Debe ser 'open' en OrderViewModel
        get() = _previewIsLoadingLista.asStateFlow()

    private val _previewErrorLista = MutableStateFlow<String?>(null)
    override val errorLista: StateFlow<String?> // Debe ser 'open' en OrderViewModel
        get() = _previewErrorLista.asStateFlow()
    // --- FIN DE DATOS PARA LA LISTA DE ÓRDENES ---

    init {
        Log.d("PreviewOrderVM", "PreviewOrderViewModel inicializado.")
        // NOTA: El bloque 'init' de la clase base OrderViewModel se ejecutará ANTES que este log.
        // Si 'OrderViewModel.init {}' llama a 'cargarListaDeOrdenesFirestore()',
        // la versión SOBRESCRITA abajo será la que se ejecute, lo cual es bueno.
        // Si 'OrderViewModel.init {}' accede directamente a Firebase (ej. Firebase.firestore),
        // y eso crashea en preview, esta preview fallará.
    }

    override fun solicitarNuevoNumeroOrden() {
        _previewNumeroOrden.value = "PV-OT-PREVIEW-${(100..999).random()}"
        Log.d("PreviewOrderVM", "Preview: Nuevo número solicitado: ${_previewNumeroOrden.value}")
    }

    override fun guardarNuevaOrdenFirestore(orden: OrdenDeTrabajo) {
        Log.d("PreviewOrderVM", "Preview: Guardar orden: ${orden.numeroOrden}")
        _previewSaveStatus.value = OrderSaveEvent.Loading
        // Simular éxito para la preview
        // En una preview real, podrías añadir un delay con viewModelScope.launch si es necesario,
        // pero este ViewModel hereda de OrderViewModel, que a su vez hereda de ViewModel,
        // por lo que viewModelScope está disponible.
        // viewModelScope.launch {
        //     delay(500) // Simular pequeño retraso
        //     _previewSaveStatus.value = OrderSaveEvent.Success(orden.numeroOrden)
        //     solicitarNuevoNumeroOrden() // Simular el comportamiento post-guardado
        // }
        // Por ahora, síncrono para simplicidad:
        _previewSaveStatus.value = OrderSaveEvent.Success(orden.numeroOrden)
        solicitarNuevoNumeroOrden()
    }

    override fun resetSaveOrderStatus() {
        _previewSaveStatus.value = OrderSaveEvent.Idle
        Log.d("PreviewOrderVM", "Preview: resetSaveOrderStatus")
    }


    override fun cargarListaDeOrdenesFirestore() { // Debe ser 'open' en OrderViewModel
        Log.d("PreviewOrderVM", "Preview: cargarListaDeOrdenesFirestore() llamado, pero no se hace nada.")

    }


}

