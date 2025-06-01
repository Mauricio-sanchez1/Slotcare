package viewmodels

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import screens.Dispositivo

/**
 * ViewModel de Dispositivo Falso para usar en Previews de Compose.
 * Sobrescribe el comportamiento que depende de Firebase o de un contexto Android real.
 */
class PreviewDeviceViewModel : DeviceViewModel() {
    // Sobrescribe el StateFlow para que no dependa de la inicialización real de Firebase
    private val _previewSaveDeviceStatus = MutableStateFlow<DeviceSaveEvent>(DeviceSaveEvent.Idle)
    override val saveDeviceStatus: StateFlow<DeviceSaveEvent> = _previewSaveDeviceStatus


    override fun guardarDispositivo(dispositivo: Dispositivo) {
        // Simula un guardado exitoso para la preview después de un pequeño retraso
        // o simplemente actualiza el estado a Success inmediatamente.
        // viewModelScope.launch {
        //     _previewSaveDeviceStatus.value = DeviceSaveEvent.Loading
        //     kotlinx.coroutines.delay(1000) // Simula demora de red
        //     _previewSaveDeviceStatus.value = DeviceSaveEvent.Success(dispositivo.idDispositivo)
        // }
        // O más simple para preview:
        _previewSaveDeviceStatus.value = DeviceSaveEvent.Success("(Preview) ${dispositivo.idDispositivo}")
        // No intentes acceder a Firebase aquí.
        // Log.d("PreviewDeviceViewModel", "Simulando guardado de dispositivo: ${dispositivo.idDispositivo}")
    }

    override fun resetSaveDeviceStatus() {
        _previewSaveDeviceStatus.value = DeviceSaveEvent.Idle
    }

    override fun generarIdDispositivoUnico(): String {
        return "PREVIEW-DEV-12345"
    }
}