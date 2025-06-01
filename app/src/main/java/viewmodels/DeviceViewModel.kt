package viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import screens.Dispositivo
import java.util.UUID

// Evento para el estado de guardado del dispositivo
sealed class DeviceSaveEvent {
    data class Success(val deviceId: String) : DeviceSaveEvent()
    data class Error(val message: String) : DeviceSaveEvent()
    object Loading : DeviceSaveEvent()
    object Idle : DeviceSaveEvent() // Estado inicial o después de un error/éxito reseteado
}

open class DeviceViewModel : ViewModel() {

    // Inicialización LAZY de Firestore. Solo se instanciará si se accede a 'db'.
    // PreviewDeviceViewModel no debería acceder a esta instancia.
    protected open val db: FirebaseFirestore by lazy {
        Log.d("DeviceViewModel", "Accediendo e inicializando 'db' (Firestore) de forma lazy.")
        Firebase.firestore
    }

    private val _saveDeviceStatus = MutableStateFlow<DeviceSaveEvent>(DeviceSaveEvent.Idle)
    open val saveDeviceStatus: StateFlow<DeviceSaveEvent> = _saveDeviceStatus.asStateFlow()

    init {
        Log.d("DeviceViewModel", "DeviceViewModel inicializado. 'db' aún no se ha accedido/inicializado.")
    }

    open fun guardarDispositivo(dispositivo: Dispositivo) {
        viewModelScope.launch {
            _saveDeviceStatus.value = DeviceSaveEvent.Loading
            Log.d("DeviceViewModel", "Intentando guardar dispositivo: ${dispositivo.idDispositivo}")

            // Validaciones (puedes expandirlas)
            if (dispositivo.idDispositivo.isBlank()) {
                _saveDeviceStatus.value = DeviceSaveEvent.Error("ID de dispositivo inválido.")
                Log.e("DeviceViewModel", "Error: ID de dispositivo no puede estar vacío.")
                return@launch
            }
            if (dispositivo.tipo.isBlank()) {
                _saveDeviceStatus.value = DeviceSaveEvent.Error("El tipo de dispositivo es requerido.")
                Log.e("DeviceViewModel", "Error: El tipo de dispositivo no puede estar vacío.")
                return@launch
            }
            if (dispositivo.ubicacion.isBlank()) {
                _saveDeviceStatus.value = DeviceSaveEvent.Error("La ubicación es requerida.")
                Log.e("DeviceViewModel", "Error: La ubicación no puede estar vacía.")
                return@launch
            }
            if (dispositivo.marca.isBlank()) {
                _saveDeviceStatus.value = DeviceSaveEvent.Error("La marca es requerida.")
                Log.e("DeviceViewModel", "Error: La marca no puede estar vacía.")
                return@launch
            }
            if (dispositivo.numeroSerie.isBlank()) {
                _saveDeviceStatus.value = DeviceSaveEvent.Error("El número de serie es requerido.")
                Log.e("DeviceViewModel", "Error: El número de serie no puede estar vacío.")
                return@launch
            }
            // Añade más validaciones si es necesario

            try {
                val collectionName = "dispositivos"
                // Aquí 'db' será accedido por primera vez si no lo ha sido antes,
                // disparando la inicialización lazy.
                db.collection(collectionName)
                    .document(dispositivo.idDispositivo)
                    .set(dispositivo)
                    .await()

                _saveDeviceStatus.value = DeviceSaveEvent.Success(dispositivo.idDispositivo)
                Log.d("DeviceViewModel", "Dispositivo guardado en Firestore con ID: ${dispositivo.idDispositivo}")

            } catch (e: Exception) {
                _saveDeviceStatus.value = DeviceSaveEvent.Error(e.message ?: "Error desconocido al guardar en Firestore")
                Log.e("DeviceViewModel", "Error al guardar dispositivo en Firestore", e)
            }
        }
    }

    open fun resetSaveDeviceStatus() {
        _saveDeviceStatus.value = DeviceSaveEvent.Idle
        Log.d("DeviceViewModel", "Estado de guardado reseteado a Idle.")
    }

    open fun generarIdDispositivoUnico(): String {
        val timestampSuffix = System.currentTimeMillis().toString().takeLast(6) // Un poco más corto
        val uuidSuffix = UUID.randomUUID().toString().substring(0, 4).uppercase()
        val generatedId = "DEV-${timestampSuffix}-${uuidSuffix}"
        Log.d("DeviceViewModel", "ID de dispositivo único generado: $generatedId")
        return generatedId
    }
}