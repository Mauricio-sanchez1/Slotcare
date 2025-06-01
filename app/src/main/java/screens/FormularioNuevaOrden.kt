package screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import components.ModernSuccessCardDialog
import viewmodels.OrderSaveEvent
import viewmodels.OrderViewModel
import viewmodels.PreviewOrderViewModel
import com.example.slotcare0011.ui.theme.SlotCare0011Theme

data class OrdenDeTrabajo(
    val numeroOrden: String = "",
    val asignadoA: String? = null,
    val ubicacion: String = "",
    val tipoDispositivo: String = "",
    val numeroSerie: String = "",
    val descripcionProblema: String = "",
    val estado: String = "Activa",
    val fechaCreacion: Long = System.currentTimeMillis(), // En Firestore es un número (Long), coincide
    val imageUrl: String? = null
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioNuevaOrden(
    onBack: () -> Unit,
    orderViewModel: OrderViewModel = viewModel()
) {
    val context = LocalContext.current
    val numeroOrdenGenerado by orderViewModel.numeroOrden.collectAsState()
    val saveStatus by orderViewModel.saveOrderStatus.collectAsState()

    // Estados para los campos del formulario (como ya los tienes)
    val textoPlaceholderAsignarA = "Asigne un técnico"
    val opcionesRealesAsignarA = listOf("Técnico A", "Técnico B", "Técnico C", "Sin asignar") // Ejemplo
    val opcionesAsignarAConPlaceholder = listOf(textoPlaceholderAsignarA) + opcionesRealesAsignarA
    var expandedAsignarA by remember { mutableStateOf(false) }
    var seleccionadoAsignarA by remember { mutableStateOf(textoPlaceholderAsignarA) }

    val textoPlaceholderTipoDispositivo = "Seleccione un Dispositivo"
    val opcionesRealesTipoDispositivo = listOf("Laptop", "PC Escritorio", "Impresora", "Tablet", "Otro") // Ejemplo
    val opcionesParaDropdownTipoDispositivo = listOf(textoPlaceholderTipoDispositivo) + opcionesRealesTipoDispositivo
    var expandedTipoDispositivo by remember { mutableStateOf(false) }
    var seleccionadoTipoDispositivo by remember { mutableStateOf(textoPlaceholderTipoDispositivo) }

    val textoPlaceholderUbicacion = "Seleccione una ubicación"
    val opcionesRealesUbicacion = listOf("Ubicación A", "Ubicación B") // Ejemplo
    val opcionesUbicacionConPlaceholder = listOf(textoPlaceholderUbicacion) + opcionesRealesUbicacion
    var expandedUbicacion by remember { mutableStateOf(false) }
    var seleccionadaUbicacion by remember { mutableStateOf(textoPlaceholderUbicacion) }

    var numeroSerie by remember { mutableStateOf("") }
    var descripcionProblema by remember { mutableStateOf("") }

    // Estado para el diálogo de éxito y el número de orden guardada
    var mostrarDialogoGuardadoExitoso by remember { mutableStateOf(false) }
    var numeroOrdenGuardadaParaDialogo by remember { mutableStateOf<String?>(null) }

    // Estado para isLoading (basado en saveStatus)
    var isLoading by remember { mutableStateOf(false) }

    // Estado para el diálogo de error (opcional, pero buena práctica)
    var mostrarDialogoError by remember { mutableStateOf(false) }
    var mensajeErrorDialogo by remember { mutableStateOf("") }

    var imagenUriSeleccionada by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            imagenUriSeleccionada = uri
        }
    )
    // Observar el estado de guardado desde el ViewModel
    LaunchedEffect(saveStatus) {
        isLoading = saveStatus is OrderSaveEvent.Loading

        when (val status: OrderSaveEvent = saveStatus) {
            is OrderSaveEvent.Success -> {
                numeroOrdenGuardadaParaDialogo = status.orderId
                mostrarDialogoGuardadoExitoso = true
                // El ViewModel ya llama a solicitarNuevoNumeroOrden() internamente
                // El reset de saveOrderStatus se hará en el onDismiss del diálogo de éxito
            }
            is OrderSaveEvent.Error -> {
                mensajeErrorDialogo = status.message
                mostrarDialogoError = true
                // El reset de saveOrderStatus se hará en el onDismiss del diálogo de error
            }
            OrderSaveEvent.Loading -> {
                // isLoading ya se actualizó
            }
            OrderSaveEvent.Idle -> {
                // No hacer nada específico aquí, isLoading ya es false
            }
        }
    }

    if (mostrarDialogoGuardadoExitoso) {
        ModernSuccessCardDialog(
            showDialog = true,
            title = "¡Orden Creada!",
            message = "La orden de trabajo N° ${numeroOrdenGuardadaParaDialogo ?: ""} ha sido creada exitosamente.",
            onDismiss = {
                mostrarDialogoGuardadoExitoso = false
                numeroOrdenGuardadaParaDialogo = null

                // --- INICIO: Limpieza de Campos ---
                seleccionadoAsignarA = textoPlaceholderAsignarA
                seleccionadaUbicacion = textoPlaceholderUbicacion
                seleccionadoTipoDispositivo = textoPlaceholderTipoDispositivo
                numeroSerie = ""
                descripcionProblema = ""
                // Resetea cualquier otro estado de campo que tengas
                // --- FIN: Limpieza de Campos ---

                orderViewModel.resetSaveOrderStatus() // Resetear el estado en el ViewModel
                // orderViewModel.solicitarNuevoNumeroOrden() // Esto ya se hace dentro de guardarNuevaOrdenFirestore
                onBack() // Navegar hacia atrás
            }
        )
    }

    if (mostrarDialogoError) {
        AlertDialog( // Un diálogo de error simple, puedes crear uno más estilizado como ModernErrorCardDialog
            onDismissRequest = {
                mostrarDialogoError = false
                orderViewModel.resetSaveOrderStatus() // Resetear el estado en el ViewModel
            },
            title = { Text("Error al Guardar") },
            text = { Text(mensajeErrorDialogo) },
            confirmButton = {
                Button(onClick = {
                    mostrarDialogoError = false
                    orderViewModel.resetSaveOrderStatus()
                }) {
                    Text("Aceptar")
                }
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
                .fillMaxSize() // Cambiado para ocupar todo el espacio disponible
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // --- HIJO 1: LA ROW DEL ENCABEZADO --- (como la tienes)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    // Antes de ir atrás, si hay cambios sin guardar y el estado no es Idle o Success,
                    // podrías preguntar al usuario si desea descartar.
                    // Por ahora, simplemente vamos atrás.
                    if (!isLoading) { // Evitar ir atrás si está guardando
                        orderViewModel.resetSaveOrderStatus() // Asegura limpiar estado si se va atrás manualmente
                        onBack()
                    }
                }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = "Nueva Orden de Trabajo",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 8.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // --- CAMPO: No. Trabajo --- (como lo tienes)
            OutlinedTextField(
                value = numeroOrdenGenerado,
                onValueChange = { /* No hacer nada */ },
                label = { Text("No. Trabajo") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            )

            //---Dropdown para Asignar A--- (como lo tienes)
            ExposedDropdownMenuBox(
                expanded = expandedAsignarA,
                onExpandedChange = { if (!isLoading) expandedAsignarA = !expandedAsignarA },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = seleccionadoAsignarA,
                    onValueChange = { },
                    label = { Text("Asignar A") },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAsignarA)
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth(),
                    enabled = !isLoading
                )
                ExposedDropdownMenu(
                    expanded = expandedAsignarA,
                    onDismissRequest = { expandedAsignarA = false }
                ) {
                    opcionesAsignarAConPlaceholder.forEach { opcionAsignar ->
                        DropdownMenuItem(
                            text = { Text(opcionAsignar) },
                            onClick = {
                                seleccionadoAsignarA = opcionAsignar
                                expandedAsignarA = false
                            },
                            enabled = opcionAsignar != textoPlaceholderAsignarA || opcionesRealesAsignarA.isEmpty()
                        )
                    }
                }
            }
            // --- Dropdown para las Ubicaciones --- (como lo tienes)
            ExposedDropdownMenuBox(
                expanded = expandedUbicacion,
                onExpandedChange = { if (!isLoading) expandedUbicacion = !expandedUbicacion },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = seleccionadaUbicacion,
                    onValueChange = { },
                    label = { Text("Ubicación") },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedUbicacion)
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth(),
                    enabled = !isLoading
                )
                ExposedDropdownMenu(
                    expanded = expandedUbicacion,
                    onDismissRequest = { expandedUbicacion = false }
                ) {
                    opcionesUbicacionConPlaceholder.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                seleccionadaUbicacion = opcion
                                expandedUbicacion = false
                            },
                            enabled = opcion != textoPlaceholderUbicacion || opcionesRealesUbicacion.isEmpty()
                        )
                    }
                }
            }

            // --- Dropdown para "Tipo de Dispositivo" --- (como lo tienes)
            ExposedDropdownMenuBox(
                expanded = expandedTipoDispositivo,
                onExpandedChange = { if (!isLoading) expandedTipoDispositivo = !expandedTipoDispositivo },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = seleccionadoTipoDispositivo,
                    onValueChange = { },
                    label = { Text("Tipo de Dispositivo") },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTipoDispositivo)
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth(),
                    enabled = !isLoading
                )
                ExposedDropdownMenu(
                    expanded = expandedTipoDispositivo,
                    onDismissRequest = { expandedTipoDispositivo = false }
                ) {
                    opcionesParaDropdownTipoDispositivo.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                seleccionadoTipoDispositivo = opcion
                                expandedTipoDispositivo = false
                            },
                            enabled = !(opcion == textoPlaceholderTipoDispositivo && opcionesRealesTipoDispositivo.isNotEmpty())
                        )
                    }
                }
            }

            // --- Campo: Número de Serie --- (como lo tienes)
            OutlinedTextField(
                value = numeroSerie,
                onValueChange = { numeroSerie = it },
                label = { Text("Número de serie") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )

            // --- Campo: Descripción del Problema --- (como lo tienes)
            OutlinedTextField(
                value = descripcionProblema,
                onValueChange = { descripcionProblema = it },
                label = { Text("Descripción del problema") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Adjuntar Foto (Opcional)",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.fillMaxWidth()
            )

            if (imagenUriSeleccionada != null) {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = imagenUriSeleccionada),
                        contentDescription = "Imagen seleccionada",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f), // O la relación de aspecto que prefieras
                        contentScale = ContentScale.Crop
                    )
                }
            }

            OutlinedButton(
                onClick = { imagePickerLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Filled.AddAPhoto,
                    contentDescription = "Icono Añadir Foto",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(if (imagenUriSeleccionada == null) "Seleccionar Foto" else "Cambiar Foto")
            }
            Spacer(modifier = Modifier.weight(1f, fill = true)) // Empuja el botón hacia abajo

            // ---Botón de Guardar---
            Button(
                onClick = {
                    if (isLoading) return@Button // Prevenir múltiples clicks mientras está cargando

                    // --- VALIDACIONES (igual que antes) ---
                    if (seleccionadoAsignarA == textoPlaceholderAsignarA) {
                        Toast.makeText(context, "Por favor, asigne un técnico.", Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    if (seleccionadaUbicacion == textoPlaceholderUbicacion) {
                        Toast.makeText(context, "Por favor, seleccione una ubicación.", Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    if (seleccionadoTipoDispositivo == textoPlaceholderTipoDispositivo) {
                        Toast.makeText(context, "Por favor, seleccione un tipo de dispositivo.", Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    if (numeroSerie.isBlank()) {
                        Toast.makeText(context, "El número de serie no puede estar vacío.", Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    if (descripcionProblema.isBlank()) {
                        Toast.makeText(context, "La descripción del problema no puede estar vacía.", Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    // --- FIN VALIDACIONES ---

                    val nuevaOrden = OrdenDeTrabajo(
                        numeroOrden = numeroOrdenGenerado, // El ViewModel usará este para el ID del documento
                        asignadoA = seleccionadoAsignarA,
                        ubicacion = seleccionadaUbicacion,
                        tipoDispositivo = seleccionadoTipoDispositivo,
                        numeroSerie = numeroSerie,
                        descripcionProblema = descripcionProblema
                    )
                    orderViewModel.guardarNuevaOrdenFirestore(nuevaOrden)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                enabled = !isLoading, // Deshabilitar si está cargando
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Guardar Orden", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        } // Fin de la Column principal scrolleable
    } // Fin del Box
}


@Preview(showBackground = true, name = "Formulario Nueva Orden Preview")
@Composable
fun FormularioNuevaOrdenPreview() {
    SlotCare0011Theme {
        FormularioNuevaOrden(
            onBack = {},
            orderViewModel = PreviewOrderViewModel() // Usando la versión de Preview
        )
    }
}


