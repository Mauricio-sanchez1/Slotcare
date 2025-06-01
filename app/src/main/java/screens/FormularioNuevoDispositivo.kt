package screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import viewmodels.DeviceSaveEvent
import viewmodels.DeviceViewModel
import components.ModernSuccessCardDialog
import viewmodels.PreviewDeviceViewModel
import com.example.slotcare0011.ui.theme.SlotCare0011Theme

data class Dispositivo(
    val idDispositivo: String,
    val tipo: String,
    val tipoEspecifico: String? = null, // Puede ser el nombre del tipo si es "Otro"
    val ubicacion: String,
    val numeroSerie: String,
    val estado: String,
    val marca: String,
    val tieneContrasena: Boolean,
    val contrasena: String? = null, // Solo si tieneContrasena es true
    val sistemaOperativo: String,
    val sistemaOperativoEspecifico: String? = null, // Puede ser el nombre del SO si es "Otro"
    val fechaCreacion: Long = System.currentTimeMillis()
    // Considera añadir imageUrl si lo vas a usar pronto
    // val imageUrl: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioNuevoDispositivo(
    onBack: () -> Unit,
    onSaveSuccessNavigation: () -> Unit,
    deviceViewModel: DeviceViewModel = viewModel() // Inyección del ViewModel real
) {
    val context = LocalContext.current

    var idDispositivo by remember { mutableStateOf("") }
    val saveStatus by deviceViewModel.saveDeviceStatus.collectAsState()
    var isLoading by remember { mutableStateOf(false) }

    // --- Tipo de Dispositivo ---
    val tiposDeDispositivoBaseOriginales by remember {
        mutableStateOf(listOf("Pantalla", "Ordenador", "Laptop", "Impresora", "Celular", "Tablet"))
    }
    val opcionOtroTipo = "Otro (Especificar)"
    val placeholderTipoDispositivo = "Seleccione tipo de dispositivo"
    var tipoSeleccionado by remember { mutableStateOf("") }
    var expandedTipo by remember { mutableStateOf(false) }
    var tipoEspecifico by remember { mutableStateOf("") }
    var mostrarCampoTipoEspecifico by remember { mutableStateOf(false) }
    val tiposDeDispositivoParaDropdown = remember(tiposDeDispositivoBaseOriginales, opcionOtroTipo) {
        tiposDeDispositivoBaseOriginales + opcionOtroTipo
    }

    // --- Ubicación ---
    val ubicacionesBaseOriginales by remember {
        mutableStateOf(
            listOf(
                "Oficina Principal",
                "Laboratorio Alpha",
                "Sala de Reuniones Beta",
                "Almacén Central"
            )
        )
    }
    val placeholderUbicacion = "Seleccione ubicación"
    var ubicacionSeleccionada by remember { mutableStateOf("") }
    var expandedUbicacion by remember { mutableStateOf(false) }
    val ubicacionesParaDropdown = ubicacionesBaseOriginales

    // --- Estado del Dispositivo ---
    val estadosDispositivo = remember { listOf("Excelente", "Bueno", "Regular", "Malo") }
    val placeholderEstado = "Indique el estado del dispositivo"
    var estadoSeleccionado by remember { mutableStateOf("") }
    var expandedEstado by remember { mutableStateOf(false) }

    // --- Sistema Operativo ---
    val sistemasOperativosBase = remember {
        listOf("Windows 10", "Windows 11", "macOS", "Android", "Linux", "Otro")
    }
    val placeholderSO = "Seleccione sistema operativo"
    var soSeleccionado by remember { mutableStateOf("") }
    var expandedSO by remember { mutableStateOf(false) }
    var soEspecifico by remember { mutableStateOf("") }
    var mostrarCampoSOEspecifico by remember { mutableStateOf(false) }

    //--- Opciones de texto ---
    var numeroSerie by remember { mutableStateOf("") }
    var marca by remember { mutableStateOf("") }

    //--- Contraseña ---
    var tieneContrasena by remember { mutableStateOf(false) }
    var contrasena by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Para el diálogo de éxito
    var mostrarDialogoExito by remember { mutableStateOf(false) }
    var idDispositivoGuardadoParaDialogo by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(saveStatus) {
        isLoading = saveStatus is DeviceSaveEvent.Loading
        Log.d("FormularioNuevoDispositivo", "SaveStatus: $saveStatus, isLoading: $isLoading")

        when (val status = saveStatus) {
            is DeviceSaveEvent.Success -> {
                Toast.makeText(context, "Dispositivo '${status.deviceId}' guardado exitosamente!", Toast.LENGTH_SHORT).show()
                idDispositivoGuardadoParaDialogo = status.deviceId
                mostrarDialogoExito = true
            }
            is DeviceSaveEvent.Error -> {
                Toast.makeText(context, "Error al guardar: ${status.message}", Toast.LENGTH_LONG).show()
                deviceViewModel.resetSaveDeviceStatus()
            }
            DeviceSaveEvent.Loading -> { /* isLoading ya se actualizó */ }
            DeviceSaveEvent.Idle -> { isLoading = false }
        }
    }

    if (mostrarDialogoExito && idDispositivoGuardadoParaDialogo != null) {
        ModernSuccessCardDialog(
            showDialog = true,
            title = "¡Dispositivo Guardado!",
            message = "El dispositivo con ID '${idDispositivoGuardadoParaDialogo}' se ha guardado correctamente.",
            onDismiss = {
                mostrarDialogoExito = false
                idDispositivoGuardadoParaDialogo = null
                deviceViewModel.resetSaveDeviceStatus()
                onSaveSuccessNavigation()
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, enabled = !isLoading) { // Deshabilitar si está cargando
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = if (isLoading) 0.38f else 1f)
                    )
                }
                Text(
                    text = "Añadir Nuevo Dispositivo",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 8.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = if (isLoading) 0.38f else 1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = idDispositivo,
                onValueChange = { idDispositivo = it },
                label = { Text("ID Dispositivo (Opcional, se autogenera)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !isLoading
            )

            // --- Tipo de Dispositivo ---
            ExposedDropdownMenuBox(
                expanded = expandedTipo,
                onExpandedChange = { if (!isLoading) expandedTipo = !expandedTipo },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = when {
                        tipoSeleccionado == opcionOtroTipo && mostrarCampoTipoEspecifico && tipoEspecifico.isNotBlank() ->
                            "${opcionOtroTipo}: ${tipoEspecifico.take(20)}${if (tipoEspecifico.length > 20) "..." else ""}"
                        tipoSeleccionado == opcionOtroTipo && mostrarCampoTipoEspecifico -> opcionOtroTipo
                        tipoSeleccionado.isNotBlank() -> tipoSeleccionado
                        else -> placeholderTipoDispositivo
                    },
                    onValueChange = {},
                    label = { Text("Tipo de Dispositivo") },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTipo) },
                    modifier = Modifier.menuAnchor(
                        type = MenuAnchorType.PrimaryNotEditable, // O MenuAnchorType.PrimaryEditable según tu caso
                        enabled = !isLoading // O simplemente 'true' si siempre está habilitado aquí
                    ).fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f), // Ajusta el alpha según tu tema
                        unfocusedTextColor = if (tipoSeleccionado.isBlank()) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface,
                        focusedTextColor = if (tipoSeleccionado.isBlank()) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface,
                    ),
                    enabled = !isLoading
                )
                ExposedDropdownMenu(
                    expanded = expandedTipo,
                    onDismissRequest = { expandedTipo = false }
                ) {
                    tiposDeDispositivoParaDropdown.forEach { tipoItem ->
                        DropdownMenuItem(
                            text = { Text(tipoItem) },
                            onClick = {
                                tipoSeleccionado = tipoItem
                                mostrarCampoTipoEspecifico = (tipoItem == opcionOtroTipo)
                                if (tipoItem != opcionOtroTipo) tipoEspecifico = ""
                                expandedTipo = false
                            },
                            enabled = !isLoading
                        )
                    }
                }
            }

            if (mostrarCampoTipoEspecifico) {
                OutlinedTextField(
                    value = tipoEspecifico,
                    onValueChange = { tipoEspecifico = it },
                    label = { Text("Especificar Tipo") },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    singleLine = true,
                    enabled = !isLoading
                )
            }

            // --- Ubicación ---
            ExposedDropdownMenuBox(
                expanded = expandedUbicacion,
                onExpandedChange = { if (!isLoading) expandedUbicacion = !expandedUbicacion },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = if (ubicacionSeleccionada.isNotBlank()) ubicacionSeleccionada else placeholderUbicacion,
                    onValueChange = {},
                    label = { Text("Ubicación") },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedUbicacion) },
                    modifier = Modifier.menuAnchor(
                        type = MenuAnchorType.PrimaryNotEditable, // O MenuAnchorType.PrimaryEditable según tu caso
                        enabled = !isLoading // O simplemente 'true' si siempre está habilitado aquí
                    ).fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                        unfocusedTextColor = if (ubicacionSeleccionada.isBlank()) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface,
                        focusedTextColor = if (ubicacionSeleccionada.isBlank()) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface,
                    ),
                    enabled = !isLoading
                )
                ExposedDropdownMenu(
                    expanded = expandedUbicacion,
                    onDismissRequest = { expandedUbicacion = false }
                ) {
                    if (ubicacionesParaDropdown.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("No hay ubicaciones disponibles") },
                            onClick = { expandedUbicacion = false },
                            enabled = false // Siempre deshabilitado si está vacío, incluso si !isLoading
                        )
                    } else {
                        ubicacionesParaDropdown.forEach { ubicacionItem ->
                            DropdownMenuItem(
                                text = { Text(ubicacionItem) },
                                onClick = {
                                    ubicacionSeleccionada = ubicacionItem
                                    expandedUbicacion = false
                                },
                                enabled = !isLoading
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                value = numeroSerie,
                onValueChange = { numeroSerie = it },
                label = { Text("Número de Serie") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !isLoading
            )

            // --- Estado del Dispositivo ---
            ExposedDropdownMenuBox(
                expanded = expandedEstado,
                onExpandedChange = { if (!isLoading) expandedEstado = !expandedEstado },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = if (estadoSeleccionado.isNotBlank()) estadoSeleccionado else placeholderEstado,
                    onValueChange = {},
                    label = { Text("Estado del Dispositivo") },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEstado) },
                    modifier = Modifier.menuAnchor(
                        type = MenuAnchorType.PrimaryNotEditable, // O MenuAnchorType.PrimaryEditable según tu caso
                        enabled = !isLoading // O simplemente 'true' si siempre está habilitado aquí
                    ).fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                        unfocusedTextColor = if (estadoSeleccionado.isBlank()) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface,
                        focusedTextColor = if (estadoSeleccionado.isBlank()) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface,
                    ),
                    enabled = !isLoading
                )
                ExposedDropdownMenu(
                    expanded = expandedEstado,
                    onDismissRequest = { expandedEstado = false }
                ) {
                    estadosDispositivo.forEach { estadoItem ->
                        DropdownMenuItem(
                            text = { Text(estadoItem) },
                            onClick = {
                                estadoSeleccionado = estadoItem
                                expandedEstado = false
                            },
                            enabled = !isLoading
                        )
                    }
                }
            }

            OutlinedTextField(
                value = marca,
                onValueChange = { marca = it },
                label = { Text("Marca") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !isLoading
            )

            // --- Sistema Operativo ---
            ExposedDropdownMenuBox(
                expanded = expandedSO,
                onExpandedChange = { if(!isLoading) expandedSO = !expandedSO },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = when {
                        soSeleccionado == "Otro" && mostrarCampoSOEspecifico && soEspecifico.isNotBlank() ->
                            "Otro: ${soEspecifico.take(20)}${if (soEspecifico.length > 20) "..." else ""}"
                        soSeleccionado == "Otro" && mostrarCampoSOEspecifico -> "Otro (Especificar)"
                        soSeleccionado.isNotBlank() -> soSeleccionado
                        else -> placeholderSO
                    },
                    onValueChange = {},
                    label = { Text("Sistema Operativo") },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSO) },
                    modifier = Modifier.menuAnchor(
                        type = MenuAnchorType.PrimaryNotEditable, // O MenuAnchorType.PrimaryEditable según tu caso
                        enabled = !isLoading // O simplemente 'true' si siempre está habilitado aquí
                    ).fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                        unfocusedTextColor = if (soSeleccionado.isBlank()) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface,
                        focusedTextColor = if (soSeleccionado.isBlank()) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface,
                    ),
                    enabled = !isLoading
                )
                ExposedDropdownMenu(
                    expanded = expandedSO,
                    onDismissRequest = { expandedSO = false }
                ) {
                    sistemasOperativosBase.forEach { soItem ->
                        DropdownMenuItem(
                            text = { Text(soItem) },
                            onClick = {
                                soSeleccionado = soItem
                                mostrarCampoSOEspecifico = (soItem == "Otro")
                                if (soItem != "Otro") soEspecifico = ""
                                expandedSO = false
                            },
                            enabled = !isLoading
                        )
                    }
                }
            }

            if (mostrarCampoSOEspecifico) {
                OutlinedTextField(
                    value = soEspecifico,
                    onValueChange = { soEspecifico = it },
                    label = { Text("Especificar Sistema Operativo") },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    singleLine = true,
                    enabled = !isLoading
                )
            }

            // --- Contraseña ---
            Row(
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp)
                    .toggleable(
                        value = tieneContrasena,
                        onValueChange = {
                            if (!isLoading) {
                                tieneContrasena = it
                                if (!it) {
                                    contrasena = ""
                                    passwordVisible = false
                                }
                            }
                        },
                        role = Role.Checkbox,
                        enabled = !isLoading
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = tieneContrasena,
                    onCheckedChange = null,
                    enabled = !isLoading
                )
                Text(
                    text = "El dispositivo tiene contraseña",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = if (isLoading) 0.38f else 1f)
                )
            }

            if (tieneContrasena) {
                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { contrasena = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                        IconButton(onClick = { passwordVisible = !passwordVisible }, enabled = !isLoading) {
                            Icon(imageVector = image, description, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = if (isLoading) 0.38f else 1f))
                        }
                    },
                    enabled = !isLoading
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (isLoading) return@Button

                    if (tipoSeleccionado.isBlank() || (tipoSeleccionado == opcionOtroTipo && tipoEspecifico.isBlank())) {
                        Toast.makeText(context, "Por favor, selecciona o especifica un tipo de dispositivo.", Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    if (ubicacionSeleccionada.isBlank()) {
                        Toast.makeText(context, "Por favor, selecciona una ubicación.", Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    if (numeroSerie.isBlank()) {
                        Toast.makeText(context, "El número de serie no puede estar vacío.", Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    if (estadoSeleccionado.isBlank()) {
                        Toast.makeText(context, "Por favor, indica el estado del dispositivo.", Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    if (marca.isBlank()) {
                        Toast.makeText(context, "La marca no puede estar vacía.", Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    if (soSeleccionado.isBlank() || (soSeleccionado == "Otro" && soEspecifico.isBlank())) {
                        Toast.makeText(context, "Por favor, selecciona o especifica un sistema operativo.", Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    if (tieneContrasena && contrasena.isBlank()) {
                        Toast.makeText(context, "Por favor, ingresa la contraseña.", Toast.LENGTH_LONG).show()
                        return@Button
                    }

                    val idFinalDispositivo = idDispositivo.trim().ifBlank {
                        deviceViewModel.generarIdDispositivoUnico()
                    }

                    val tipoFinalParaGuardar = if (tipoSeleccionado == opcionOtroTipo) tipoEspecifico.trim() else tipoSeleccionado
                    val tipoEspecificoParaGuardar = if (tipoSeleccionado == opcionOtroTipo) tipoEspecifico.trim() else null

                    val sistemaOperativoFinal = if (soSeleccionado == "Otro") soEspecifico.trim() else soSeleccionado
                    val sistemaOperativoEspecificoFinal = if (soSeleccionado == "Otro") soEspecifico.trim() else null

                    val nuevoDispositivo = Dispositivo(
                        idDispositivo = idFinalDispositivo,
                        tipo = tipoFinalParaGuardar,
                        tipoEspecifico = tipoEspecificoParaGuardar,
                        ubicacion = ubicacionSeleccionada,
                        numeroSerie = numeroSerie.trim(),
                        estado = estadoSeleccionado,
                        marca = marca.trim(),
                        tieneContrasena = tieneContrasena,
                        contrasena = if (tieneContrasena) contrasena.trim() else null,
                        sistemaOperativo = sistemaOperativoFinal,
                        sistemaOperativoEspecifico = sistemaOperativoEspecificoFinal
                    )

                    Log.d("FormularioNuevoDispositivo", "Guardando Dispositivo: $nuevoDispositivo")
                    deviceViewModel.guardarDispositivo(nuevoDispositivo)
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),//.padding(horizontal = 16.dp) // Considera si el padding es necesario o si quieres que ocupe todo el ancho
                shape = RoundedCornerShape(8.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.38f),
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.38f)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary, // Usar onPrimary para contraste
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Guardar Dispositivo", style = MaterialTheme.typography.labelLarge)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, name = "Formulario Nuevo Dispositivo Preview")
@Composable
fun FormularioNuevoDispositivoPreview() {
    SlotCare0011Theme {
        FormularioNuevoDispositivo(
            onBack = { Log.d("Preview", "onBack llamado") },
            onSaveSuccessNavigation = { Log.d("Preview", "onSaveSuccessNavigation llamado") },
            deviceViewModel = PreviewDeviceViewModel() // <--- AQUÍ SE USA
        )
    }
}