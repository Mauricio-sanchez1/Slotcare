package screens

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import components.ModernSuccessCardDialog
import others.Tecnico
import utils.isPreview
import com.example.slotcare0011.ui.theme.SlotCare0011Theme
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


// --- COMPOSABLE PRINCIPAL DEL FORMULARIO ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoTecnico(
    onBack: () -> Unit,
    onSaveSuccessNavigation: () -> Unit
) {
    val context = LocalContext.current
    val inPreviewMode = isPreview()

    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var identificacion by remember { mutableStateOf("") }
    var cargo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var fechaNacimientoStr by remember { mutableStateOf("") }

    var mostrarDatePickerDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val formatoFecha = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    }

    if (mostrarDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = { mostrarDatePickerDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDatePickerDialog = false
                        datePickerState.selectedDateMillis?.let { millis ->
                            fechaNacimientoStr = formatoFecha.format(Date(millis))
                        }
                    }
                ) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(
                    onClick = { mostrarDatePickerDialog = false }
                ) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    var mostrarDialogoExito by remember { mutableStateOf(false) }
    var nombreTecnicoGuardadoParaDialogo by remember { mutableStateOf<String?>(null) }

    if (mostrarDialogoExito) {
        ModernSuccessCardDialog(
            showDialog = true, // o `mostrarDialogoExito`
            title = "¡Técnico Guardado!",
            message = "El técnico '${nombreTecnicoGuardadoParaDialogo ?: ""}' se ha guardado correctamente.",
            onDismiss = {
                mostrarDialogoExito = false
                nombreTecnicoGuardadoParaDialogo = null
                onSaveSuccessNavigation()
            }
        )
    }
    val db = if (!inPreviewMode) {
        remember { Firebase.firestore }
    } else {
        null // En modo Preview, db será null
    }

    fun guardarTecnicoEnFirebaseDirectamente() {
        if (db == null) {
            Toast.makeText(
                context,
                "Operación de guardado no disponible en modo preview",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val tecnicoAGuardar = Tecnico(
            nombres = nombres.trim(),
            apellidos = apellidos.trim(),
            identificacion = identificacion.trim(),
            cargo = cargo.trim(),
            telefono = telefono.trim(),
            correo = correo.trim(),
            direccion = direccion.trim(),
            fechaNacimiento = fechaNacimientoStr
        )

        db.collection("Personal")
            .add(tecnicoAGuardar)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(
                    context,
                    "Guardado en Firebase con ID: ${documentReference.id}",
                    Toast.LENGTH_LONG
                ).show()
                nombreTecnicoGuardadoParaDialogo =
                    "${tecnicoAGuardar.nombres} ${tecnicoAGuardar.apellidos}"
                mostrarDialogoExito = true
                // Limpiar campos
                nombres = ""
                apellidos = ""
                identificacion = ""
                cargo = ""
                telefono = ""
                correo = ""
                direccion = ""
                fechaNacimientoStr = ""
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    context,
                    "Error al guardar en Firebase: ${e.localizedMessage}", // Usar localizedMessage
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background), // Usar color del tema
        contentAlignment = Alignment.TopCenter // Cambiado a TopCenter para un mejor flujo de formulario
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth() // Usa fillMaxWidth y deja que el scroll maneje la altura
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp) // Espaciado consistente
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = "Añadir Nuevo Técnico",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 8.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = nombres,
                onValueChange = { nuevoValor ->
                    if (nuevoValor.all { it.isLetter() || it.isWhitespace() }) {
                        nombres = nuevoValor
                    }
                },
                label = { Text("Nombres") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = apellidos,
                onValueChange = { nuevoValor ->
                    if (nuevoValor.all { it.isLetter() || it.isWhitespace() }) {
                        apellidos = nuevoValor
                    }
                },
                label = { Text("Apellidos") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = identificacion,
                onValueChange = { identificacion = it },
                label = { Text("Identificación") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = cargo,
                onValueChange = { nuevoValor ->
                    if (nuevoValor.all { it.isLetter() || it.isWhitespace() }) {
                        cargo = nuevoValor
                    }
                },
                label = { Text("Cargo") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo Electrónico") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección") },
                singleLine = false, // Dirección puede necesitar múltiples líneas
                maxLines = 3,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = fechaNacimientoStr,
                onValueChange = { nuevoValor ->
                    if (nuevoValor.all { it.isDigit() || it == '/' } && nuevoValor.length <= 10) {
                        fechaNacimientoStr = nuevoValor
                    }
                },
                label = { Text("Fecha de Nacimiento (dd/mm/aaaa)") },
                placeholder = { Text("Ej: 25/12/1990") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                readOnly = false, // <-- CAMBIADO a false para permitir entrada manual
                trailingIcon = {
                    IconButton(onClick = { mostrarDatePickerDialog = true }) {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = "Seleccionar Fecha / Abrir Calendario"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp)) // Espacio antes del botón

            Button(
                onClick = {
                    // Validaciones (como las tenías)
                    if (nombres.isBlank()) {
                        Toast.makeText(
                            context,
                            "El nombre no puede estar vacío.",
                            Toast.LENGTH_LONG
                        ).show()
                        return@Button
                    }
                    if (apellidos.isBlank()) {
                        Toast.makeText(
                            context,
                            "Los apellidos no pueden estar vacíos.",
                            Toast.LENGTH_LONG
                        ).show()
                        return@Button
                    }
                    if (identificacion.isBlank()) {
                        Toast.makeText(
                            context,
                            "La identificación no puede estar vacía.",
                            Toast.LENGTH_LONG
                        ).show()
                        return@Button
                    }
                    if (cargo.isBlank()) {
                        Toast.makeText(context, "El cargo no puede estar vacío.", Toast.LENGTH_LONG)
                            .show()
                        return@Button
                    }
                    if (telefono.isBlank()) {
                        Toast.makeText(
                            context,
                            "El teléfono no puede estar vacío.",
                            Toast.LENGTH_LONG
                        ).show()
                        return@Button
                    }
                    if (correo.isBlank()) {
                        Toast.makeText(
                            context,
                            "El correo electrónico no puede estar vacío.",
                            Toast.LENGTH_LONG
                        ).show()
                        return@Button
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(correo.trim())
                            .matches()
                    ) {
                        Toast.makeText(
                            context,
                            "El formato del correo electrónico no es válido.",
                            Toast.LENGTH_LONG
                        ).show()
                        return@Button
                    }
                    if (direccion.isBlank()) {
                        Toast.makeText(
                            context,
                            "La dirección no puede estar vacía.",
                            Toast.LENGTH_LONG
                        ).show()
                        return@Button
                    }
                    if (fechaNacimientoStr.isBlank()) {
                        Toast.makeText(
                            context,
                            "La fecha de nacimiento no puede estar vacía.",
                            Toast.LENGTH_LONG
                        ).show()
                        return@Button
                    } else {
                        try {
                            formatoFecha.isLenient = false
                            formatoFecha.parse(fechaNacimientoStr) // Validar formato
                        } catch (e: ParseException) {
                            Toast.makeText(
                                context,
                                "Formato de fecha inválido. Usar dd/mm/aaaa.",
                                Toast.LENGTH_LONG
                            ).show()
                            return@Button
                        } catch (e: Exception) { // Captura genérica por si acaso
                            Toast.makeText(context, "Error al validar la fecha.", Toast.LENGTH_LONG)
                                .show()
                            return@Button
                        }
                    }
                    // Fin Validaciones
                    // Si todo está bien, guardar en Firebase
                    guardarTecnicoEnFirebaseDirectamente()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp) // Altura estándar para botones
                    .padding(horizontal = 16.dp), // Padding horizontal para que no ocupe todo el borde
                shape = RoundedCornerShape(8.dp), // Bordes redondeados
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary)
            ) {
                Text(
                    "Guardar Técnico",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Spacer(modifier = Modifier.height(16.dp)) // Espacio al final del scroll
        }
    }
}


// --- PREVIEW DEL COMPOSABLE ---
@Preview(showBackground = true, name = "Formulario Nuevo Técnico Preview")
@Composable
fun NuevoTecnicoPreview() {
    SlotCare0011Theme { // Siempre envuelve tus previews en el tema de tu app
        NuevoTecnico(
            onBack = {},
            onSaveSuccessNavigation = {}
        )
    }
}