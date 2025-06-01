package screens

// Importaciones necesarias para Compose
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.slotcare0011.R
import com.example.slotcare0011.ui.theme.SlotCare0011Theme

// Función principal del composable
@Composable
fun SlotCareLoginScreen(
    onBack: () -> Unit,           // Callback para el botón de retroceso
    onLoginSuccess: () -> Unit    // Callback para login exitoso
) {
    // Estado para el campo de email (valor + función para actualizarlo)
    var email by remember { mutableStateOf("") }

    // Estado para el campo de contraseña
    var password by remember { mutableStateOf("") }

    // Estado para mensajes de error (null cuando no hay error)
    var loginErrorMessage by remember { mutableStateOf<String?>(null) }

    // Contenedor principal que ocupa toda la pantalla
    Box(
        modifier = Modifier
            .fillMaxSize()         // Ocupa todo el espacio disponible
            .background(Color.Transparent)  // Fondo transparente
    ) {
        // Imagen de fondo que cubre toda la pantalla
        Image(
            painter = painterResource(id = R.drawable.background_3), // Recurso de imagen
            contentDescription = "Fondo decorativo",  // Texto para accesibilidad
            modifier = Modifier.fillMaxSize(),        // Ocupa todo el espacio
            contentScale = ContentScale.Crop          // Recorta la imagen para cubrir
        )

        // Columna para organizar elementos verticalmente
        Column(
            modifier = Modifier.fillMaxSize()  // Ocupa todo el espacio disponible
        ) {
            // Fila para el botón de retroceso
            Row(
                modifier = Modifier
                    .fillMaxWidth()           // Ancho completo
                    .padding(16.dp),          // Espaciado interno
                horizontalArrangement = Arrangement.Start  // Alineación a la izquierda
            ) {
                // Botón de icono para retroceder
                IconButton(
                    onClick = onBack,         // Acción al hacer clic
                    modifier = Modifier.size(48.dp)  // Tamaño fijo
                ) {
                    // Icono de flecha hacia atrás
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver atrás",  // Descripción para accesibilidad
                        tint = Color.White  // Color blanco para el icono
                    )
                }
            }

            // Contenedor para centrar el logo
            Box(
                modifier = Modifier.fillMaxWidth(),  // Ancho completo
                contentAlignment = Alignment.Center  // Centra el contenido
            ) {
                // Logo de la aplicación
                Image(
                    painter = painterResource(R.drawable.logoslotcarepng),
                    contentDescription = "Logo de Slot Care",  // Texto descriptivo
                    modifier = Modifier
                        .padding(top = 16.dp)  // Espaciado superior
                )
            }

            // Superficie para el formulario (con bordes redondeados arriba)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()  // Ancho completo
                    .weight(1f),     // Ocupa el espacio restante
                shape = RoundedCornerShape(
                    topStart = 32.dp,  // Radio esquina superior izquierda
                    topEnd = 32.dp     // Radio esquina superior derecha
                ),
                color = Color.White.copy(alpha = 0.95f),  // Blanco semitransparente
                tonalElevation = 8.dp  // Sombra sutil
            ) {
                // Columna interna para los elementos del formulario
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 32.dp),  // Espaciado interno
                    verticalArrangement = Arrangement.spacedBy(24.dp)  // Espacio entre elementos
                ) {
                    // Columna para agrupar los campos de texto
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)  // Espacio entre campos
                    ) {
                        // Campo de texto para el email
                        OutlinedTextField(
                            value = email,  // Valor actual del campo
                            onValueChange = {
                                email = it  // Actualiza el valor
                                loginErrorMessage = null  // Limpia errores al editar
                            },
                            label = { Text("Correo Electrónico") },  // Texto de etiqueta
                            leadingIcon = {  // Icono principal
                                Icon(
                                    Icons.Filled.Email,  // Icono de email
                                    contentDescription = "Icono de correo",  // Descripción
                                    tint = Color.Gray
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),  // Ancho completo
                            shape = RoundedCornerShape(12.dp),   // Bordes redondeados
                            singleLine = true,  // Una sola línea
                            colors = OutlinedTextFieldDefaults.colors(  // Colores personalizados
                                focusedBorderColor = MaterialTheme.colorScheme.primary,  // Borde activo
                                unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),   // Borde inactivo
                                cursorColor = MaterialTheme.colorScheme.primary  // Color del cursor
                            )
                        )

                        // Campo de texto para la contraseña
                        // Estado para controlar la visibilidad de la contraseña
                        var passwordVisible by remember { mutableStateOf(false) }

// Campo de texto para la contraseña
                        OutlinedTextField(
                            value = password,
                            onValueChange = {
                                password = it
                                loginErrorMessage = null
                            },
                            label = { Text("Contraseña") },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Lock,
                                    contentDescription = "Icono de contraseña",
                                    tint = Color.Gray
                                )
                            },
                            trailingIcon = {
                                // Icono para alternar visibilidad
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                        contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                                        tint = Color.Gray
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible) {
                                VisualTransformation.None // Muestra texto plano
                            } else {
                                PasswordVisualTransformation() // Oculta texto
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                                cursorColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }

                    // Mensaje de error (solo visible cuando hay error)
                    if (!loginErrorMessage.isNullOrEmpty()) {
                        Text(
                            text = loginErrorMessage.orEmpty(),  // Texto del error
                            color = MaterialTheme.colorScheme.error,  // Color de error
                            style = MaterialTheme.typography.bodySmall,  // Estilo pequeño
                            modifier = Modifier.padding(horizontal = 8.dp)  // Espaciado
                        )
                    }

                    // Botón de inicio de sesión
                    Button(
                        onClick = {  // Acción al hacer clic
                            when {  // Validación de campos
                                email.isBlank() || password.isBlank() -> {
                                    loginErrorMessage = "Por favor complete todos los campos"
                                }
                                email == "test@example.com" && password == "password" -> {
                                    onLoginSuccess()  // Login exitoso
                                }
                                else -> {
                                    loginErrorMessage = "Credenciales incorrectas"
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()  // Ancho completo
                            .height(48.dp)   // Altura fija
                            .background(      // Fondo con gradiente
                                brush = Brush.verticalGradient(  // Gradiente vertical
                                    colors = listOf(
                                        Color(0xFFF97316),  // Naranja (arriba)
                                        Color(0xFFFFD700)   // Amarillo (abajo)
                                    )
                                ),
                                shape = RoundedCornerShape(24.dp)  // Forma de píldora
                            ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,  // Fondo transparente
                            contentColor = Color.White           // Texto blanco
                        ),
                        elevation = null  // Sin sombra
                    ) {
                        // Texto del botón
                        Text(
                            text = "Iniciar sesión",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,  // Negrita
                                letterSpacing = 0.5.sp        // Espaciado entre letras
                            )
                        )
                    }

                    // Fila para enlaces inferiores
                    Row(
                        modifier = Modifier.fillMaxWidth(),  // Ancho completo
                        horizontalArrangement = Arrangement.SpaceBetween  // Espaciado entre elementos
                    ) {
                        // Botón de texto "Olvidé contraseña"
                        TextButton(
                            onClick = { /* TODO: Implementar navegación */ }
                        ) {
                            Text("¿Olvidó su contraseña?")  // Texto del enlace
                            Color.Gray
                        }

                        // Botón de texto "Registrarse"
                        TextButton(
                            onClick = { /* TODO: Implementar navegación */ }
                        ) {
                            Text(
                                "Registrarse",  // Texto del enlace
                                fontWeight = FontWeight.Bold,  // Negrita
                                color = Color.Blue

                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SlotCareLoginScreenPreview() {
    SlotCare0011Theme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SlotCareLoginScreen(
                onBack = {},
                onLoginSuccess = {}
            )
        }
    }
}
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SlotCareLoginScreenDarkPreview() {
    SlotCare0011Theme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SlotCareLoginScreen(
                onBack = {},
                onLoginSuccess = {}
            )
        }
    }
}