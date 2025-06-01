package viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import components.EstadoOrden
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import screens.OrdenDeTrabajo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID


sealed class OrderSaveEvent {
    data class Success(val orderId: String) : OrderSaveEvent()
    data class Error(val message: String) : OrderSaveEvent()
    object Loading : OrderSaveEvent()
    object Idle : OrderSaveEvent()
}

open class OrderViewModel : ViewModel() {

    protected open val db: FirebaseFirestore by lazy {
        Log.d("OrderViewModel", "Firestore DB inicializado (lazy)")
        Firebase.firestore
    }
    protected open val ordenesCollection by lazy {
        Log.d("OrderViewModel", "Firestore 'ordenesDeTrabajo' collection obtenida (lazy)")
        db.collection("ordenesDeTrabajo")
    }

    // --- Para FormularioNuevaOrden ---
    private val _numeroOrden = MutableStateFlow("")
    open val numeroOrden: StateFlow<String> get() = _numeroOrden.asStateFlow()

    private val _saveOrderStatus = MutableStateFlow<OrderSaveEvent>(OrderSaveEvent.Idle)
    open val saveOrderStatus: StateFlow<OrderSaveEvent> get() = _saveOrderStatus.asStateFlow()

    // --- Para PantallaInicio (Lista general) ---
    private val _listaDeOrdenes = MutableStateFlow<List<OrdenDeTrabajo>>(emptyList())
    open val listaDeOrdenes: StateFlow<List<OrdenDeTrabajo>> get() = _listaDeOrdenes.asStateFlow()

    private val _isLoadingLista = MutableStateFlow(false)
    open val isLoadingLista: StateFlow<Boolean> get() = _isLoadingLista.asStateFlow()

    private val _errorLista = MutableStateFlow<String?>(null)
    open val errorLista: StateFlow<String?> get() = _errorLista.asStateFlow()

    // --- MIEMBROS NUEVOS Y MODIFICADOS PARA OrderDetailScreen ---
    private val _selectedOrder = MutableStateFlow<OrdenDeTrabajo?>(null)
    open val selectedOrder: StateFlow<OrdenDeTrabajo?> = _selectedOrder.asStateFlow()

    private val _isLoadingDetail = MutableStateFlow(false)
    open val isLoadingDetail: StateFlow<Boolean> = _isLoadingDetail.asStateFlow()

    private val _errorDetail = MutableStateFlow<String?>(null)
    open val errorDetail: StateFlow<String?> = _errorDetail.asStateFlow()

    private val _listaTecnicos = MutableStateFlow<List<String>>(emptyList())
    open val listaTecnicos: StateFlow<List<String>> = _listaTecnicos.asStateFlow()

    // Asegúrate que EstadoOrden esté definido e importado correctamente.
    // Si no está definida en este archivo, importa la clase/enum EstadoOrden.
    open val listaEstadosPosibles: List<EstadoOrden> by lazy { EstadoOrden.entries.toList() }
    // --- FIN DE MIEMBROS NUEVOS ---


    init {
        Log.d("OrderViewModel", "OrderViewModel init {} - INICIO")
        _numeroOrden.value = generarSiguienteNumeroOrdenInterno()
        Log.d("OrderViewModel", "OrderViewModel init {} - FIN. Número de orden inicial: ${_numeroOrden.value}")
        cargarListaDeTecnicos() // Cargar técnicos al iniciar el ViewModel
    }

    private fun generarSiguienteNumeroOrdenInterno(): String {
        Log.d("OrderViewModel", "generarSiguienteNumeroOrdenInterno() called")
        try {
            val formatoFechaHora = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.getDefault())
            val fechaHoraActualStr = formatoFechaHora.format(Date())
            val randomSuffix = UUID.randomUUID().toString().substring(0, 6).uppercase(Locale.ROOT)
            val numeroGenerado = "OT-${fechaHoraActualStr}-${randomSuffix}"
            Log.d("OrderViewModel", "Número generado internamente: $numeroGenerado")
            return numeroGenerado
        } catch (e: Throwable) {
            Log.e("OrderViewModel", "Error in generarSiguienteNumeroOrdenInterno: ${e.message}", e)
            return "ERROR-NUM-ORDEN" // Fallback
        }
    }

    open fun solicitarNuevoNumeroOrden() {
        _numeroOrden.value = generarSiguienteNumeroOrdenInterno()
        Log.d("OrderViewModel", "Nuevo número de orden solicitado (versión interna): ${_numeroOrden.value}")
    }

    open fun guardarNuevaOrdenFirestore(orden: OrdenDeTrabajo) {
        viewModelScope.launch {
            _saveOrderStatus.value = OrderSaveEvent.Loading
            Log.d("OrderViewModel", "Intentando guardar orden: ${orden.numeroOrden}")

            try {
                val numeroDeOrdenActual = _numeroOrden.value
                if (numeroDeOrdenActual.startsWith("ERROR")) {
                    _saveOrderStatus.value = OrderSaveEvent.Error("Número de orden inválido.")
                    return@launch
                }

                val ordenParaGuardar = orden.copy(
                    numeroOrden = numeroDeOrdenActual,
                    // Asegúrate que EstadoOrden.ACTIVA.displayName sea el string correcto para "Activa"
                    estado = EstadoOrden.ACTIVA.displayName,
                    fechaCreacion = System.currentTimeMillis()
                )

                ordenesCollection
                    .document(ordenParaGuardar.numeroOrden)
                    .set(ordenParaGuardar)
                    .await()

                _saveOrderStatus.value = OrderSaveEvent.Success(ordenParaGuardar.numeroOrden)
                Log.d("OrderViewModel", "Orden guardada en Firestore: ${ordenParaGuardar.numeroOrden}")

                // Actualizar lista local y solicitar nuevo número
                _listaDeOrdenes.value = listOf(ordenParaGuardar) + _listaDeOrdenes.value
                solicitarNuevoNumeroOrden()

            } catch (e: Exception) {
                _saveOrderStatus.value = OrderSaveEvent.Error(e.message ?: "Error al guardar en Firestore")
                Log.e("OrderViewModel", "Error al guardar orden en Firestore", e)
            }
        }
    }

    open fun resetSaveOrderStatus() {
        _saveOrderStatus.value = OrderSaveEvent.Idle
    }

    open fun cargarListaDeOrdenesFirestore() {
        viewModelScope.launch {
            _isLoadingLista.value = true
            _errorLista.value = null
            Log.d("OrderViewModel", "Iniciando carga de lista de órdenes desde Firestore...")
            try {
                val snapshot = ordenesCollection
                    .orderBy("fechaCreacion", Query.Direction.DESCENDING)
                    .get()
                    .await()
                Log.d("OrderViewModel", "Snapshot para lista obtenido. Documentos: ${snapshot.size()}")
                _listaDeOrdenes.value = snapshot.toObjects(OrdenDeTrabajo::class.java)
            } catch (e: Exception) {
                Log.e("OrderViewModel", "Error al cargar lista de órdenes", e)
                _errorLista.value = "Error al cargar órdenes: ${e.message}"
                _listaDeOrdenes.value = emptyList()
            } finally {
                _isLoadingLista.value = false
            }
        }
    }

    // --- NUEVAS FUNCIONES PARA OrderDetailScreen ---

    open fun cargarDetalleOrden(orderId: String) {
        if (orderId.isBlank()) {
            _errorDetail.value = "ID de orden inválido."
            _selectedOrder.value = null
            return
        }
        viewModelScope.launch {
            _isLoadingDetail.value = true
            _errorDetail.value = null
            Log.d("OrderViewModel", "Cargando detalle para orden ID: $orderId")
            try {
                val documentSnapshot = ordenesCollection.document(orderId).get().await()
                if (documentSnapshot.exists()) {
                    _selectedOrder.value = documentSnapshot.toObject(OrdenDeTrabajo::class.java)
                    Log.d("OrderViewModel", "Orden cargada: ${_selectedOrder.value}")
                } else {
                    _errorDetail.value = "No se encontró la orden con ID: $orderId"
                    _selectedOrder.value = null
                    Log.w("OrderViewModel", "No se encontró la orden: $orderId")
                }
            } catch (e: Exception) {
                Log.e("OrderViewModel", "Error al cargar detalle de orden $orderId", e)
                _errorDetail.value = "Error al cargar detalles: ${e.message}"
                _selectedOrder.value = null
            } finally {
                _isLoadingDetail.value = false
            }
        }
    }

    open fun limpiarDetalleOrden() {
        Log.d("OrderViewModel", "Limpiando detalle de orden.")
        _selectedOrder.value = null
        _isLoadingDetail.value = false
        _errorDetail.value = null
    }

    open fun cargarListaDeTecnicos() {
        viewModelScope.launch {
            // TODO: Implementar carga de técnicos desde Firestore o fuente de datos
            // Por ahora, una lista fija:
            val tecnicosSimulados = listOf("Técnico Alpha", "Técnico Beta", "Técnico Gamma", "Sin asignar")
            _listaTecnicos.value = tecnicosSimulados
            Log.d("OrderViewModel", "Lista de técnicos cargada/simulada: $tecnicosSimulados")
        }
    }

    open fun actualizarTecnicoDeOrden(orderId: String, nuevoTecnico: String?) {
        if (orderId.isBlank()) {
            Log.e("OrderViewModel", "actualizarTecnicoDeOrden: ID de orden vacío.")
            return
        }
        viewModelScope.launch {
            _isLoadingDetail.value = true // Podrías usar un flag de carga específico para actualizaciones
            try {
                val tecnicoParaGuardar = if (nuevoTecnico == "Sin asignar") null else nuevoTecnico
                ordenesCollection.document(orderId).update("asignadoA", tecnicoParaGuardar).await()
                // Refrescar la orden seleccionada para que la UI se actualice
                _selectedOrder.value = _selectedOrder.value?.copy(asignadoA = tecnicoParaGuardar)
                Log.d("OrderViewModel", "Técnico actualizado para $orderId a: $tecnicoParaGuardar")
            } catch (e: Exception) {
                Log.e("OrderViewModel", "Error al actualizar técnico para $orderId", e)
                _errorDetail.value = "Error al actualizar técnico: ${e.message}" // O un mensaje más amigable
            } finally {
                _isLoadingDetail.value = false
            }
        }
    }

    open fun actualizarEstadoDeOrden(orderId: String, nuevoEstado: EstadoOrden) {
        if (orderId.isBlank()) {
            Log.e("OrderViewModel", "actualizarEstadoDeOrden: ID de orden vacío.")
            return
        }
        viewModelScope.launch {
            _isLoadingDetail.value = true // Podrías usar un flag de carga específico
            try {
                ordenesCollection.document(orderId).update("estado", nuevoEstado.displayName).await()
                // Refrescar la orden seleccionada
                _selectedOrder.value = _selectedOrder.value?.copy(estado = nuevoEstado.displayName)
                Log.d("OrderViewModel", "Estado actualizado para $orderId a: ${nuevoEstado.displayName}")
            } catch (e: Exception) {
                Log.e("OrderViewModel", "Error al actualizar estado para $orderId", e)
                _errorDetail.value = "Error al actualizar estado: ${e.message}"
            } finally {
                _isLoadingDetail.value = false
            }
        }
    }

    open fun finalizarOrden(orderId: String) {
        Log.d("OrderViewModel", "Finalizando orden: $orderId")
        actualizarEstadoDeOrden(orderId, EstadoOrden.FINALIZADA)
    }
    // --- FIN DE NUEVAS FUNCIONES ---
}