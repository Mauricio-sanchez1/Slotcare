package screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.slotcare0011.ui.theme.SlotCare0011Theme

@Composable
fun SeguimientoOtScreen(
    navController: NavController = rememberNavController(),
    estado: String = "Activo", // "Entregado", "En camino", "Pendiente"
    tipoDispositivo: String = "Impresora Láser",
    numeroSerie: String = "SN-123456",
    tecnicoAsignado: String = "Juan Pérez"
) {
    // Colores del tema adaptados al diseño de la imagen
    val darkBlue = Color(0xFF1A237E)  // Azul oscuro para títulos
    val lightBlue = Color(0xFFE8EAF6) // Azul claro para fondo
    val statusColor = when (estado) {
        "activa" -> Color(0xFF4CAF50)  // Verde
        "En proceso" -> Color(0xFF2196F3)  // Amarillo
        "Finalizada" -> Color(0xFF9E9E9E)  // Gris
        "No Asignada" -> Color(0xFFF44336)  // Rojo
        "Pendiente" -> Color(0xFFFFC107)  // Amarillo/Naranja
        "Cancelada" -> Color(0xFFB00020)  // Rojo oscuro
        else -> Color.Gray
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(lightBlue)
            .verticalScroll(rememberScrollState())
    ) {
        // Barra superior azul oscuro
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(darkBlue)
                .padding(16.dp)
        ) {
            // Organización en Columna principal
            Column {
                // Fila para la flecha y espacio
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Flecha de retroceso
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás",
                            tint = Color.White
                        )
                    }
                }
                // Contenido de texto debajo de la flecha
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp) // Ajusta según el tamaño de tu flecha
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description, // Icono de documento
                            contentDescription = "Orden de trabajo",
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "No. Orden de trabajo",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                    }
                    Text(
                        text = "OT-20253005-0254-52015",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(MaterialTheme.shapes.small)
                                .background(statusColor)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = estado,
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                    Text(
                        text = "2 mayo 2025",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                    Text(
                        text = "Oficina Principal",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }

        // Contenido principal
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Información del dispositivo
            Text(
                text = "Información del dispositivo",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Tipo: $tipoDispositivo",
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "Serie: $numeroSerie",
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    text = "Técnico: $tecnicoAsignado",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = { /* Aquí va tu lógica para editar */ },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar técnico",
                        tint = Color.Black
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp,
                color = Color.LightGray.copy(alpha = 0.5f)
            )
        }
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Información del dispositivo
            Text(
                text = "Descripcion",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "EL dispositivo falla al encender jhsadkjhs dhjhjkhsahk ajhd ksajhd skjhs dkjh",
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp,
                color = Color.LightGray.copy(alpha = 0.5f)
            )
        }
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Información del dispositivo
            Text(
                text = "Imagenes",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )


            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp,
                color = Color.LightGray.copy(alpha = 0.5f)
            )
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF2196F3), // Amarillo (arriba)
                                Color(0xFF1A237E)  // Naranja (abajo) - Orden invertido
                            ),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                elevation = null
            ) {
                Text(
                    text = "Editar Orden", // Texto modificado para claridad
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF2196F3), // Amarillo (arriba)
                                Color(0xFF1A237E)  // Naranja (abajo) - Orden invertido
                            ),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                elevation = null
            ) {
                Text(
                    text = "Finalizar Orden", // Texto modificado para claridad
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                )
            }
        }
    }
}


@Preview(name = "Modo Claro", showBackground = true)
@Composable
fun SeguimientoOtLightPreview() {
    SlotCare0011Theme {
        SeguimientoOtScreen(
            estado = "En proceso",
            tipoDispositivo = "Impresora Láser HP",
            numeroSerie = "SN-789456123",
            tecnicoAsignado = "Carlos Rodríguez"
        )
    }
}

@Preview(
    name = "Modo Oscuro",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SeguimientoOtDarkPreview() {
    SlotCare0011Theme {
        SeguimientoOtScreen(
            estado = "Finalizada",
            tipoDispositivo = "Proyector Epson",
            numeroSerie = "XP-987654321",
            tecnicoAsignado = "María González"
        )
    }
}


//// Contenido principal
//        Column(
//            modifier = Modifier
//                .padding(16.dp)
//                .fillMaxWidth()
//        ) {
//            // Tarjeta de seguimiento
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                colors = CardDefaults.cardColors(
//                    containerColor = Color.White
//                ),
//                elevation = CardDefaults.cardElevation(4.dp)
//            ) {
//                Column(
//                    modifier = Modifier.padding(16.dp)
//                ) {
//                    // Título con fondo azul oscuro
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .background(darkBlue)
//                            .padding(8.dp)
//                    ) {
//                        Text(
//                            text = "Seguimiento envío",
//                            color = Color.White,
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    // Código de envío
//                    Text(
//                        text = "Código de envío",
//                        color = darkBlue,
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.Medium
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text(
//                        text = "PQ8BRK0772490230132001F",
//                        color = Color.Black,
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text(
//                        text = "Enseña este código en tu oficina de Correos para recoger el envío.",
//                        color = Color.Gray,
//                        fontSize = 12.sp
//                    )
//
//                    Divider(
//                        modifier = Modifier.padding(vertical = 16.dp),
//                        color = Color.LightGray.copy(alpha = 0.5f)
//                    )
//
//                    // Sección Origen
//                    Text(
//                        text = "Origen",
//                        color = darkBlue,
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.Medium
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text(
//                        text = "Remitente",
//                        color = Color.Gray,
//                        fontSize = 12.sp
//                    )
//                    Text(
//                        text = "ALMU****RA",
//                        color = Color.Black,
//                        fontSize = 16.sp
//                    )
//                    Text(
//                        text = "14140, LA VICTORIA",
//                        color = Color.Gray,
//                        fontSize = 14.sp
//                    )
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    // Sección Destino
//                    Text(
//                        text = "Destino",
//                        color = darkBlue,
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.Medium
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text(
//                        text = "Destinatario",
//                        color = Color.Gray,
//                        fontSize = 12.sp
//                    )
//                    Text(
//                        text = "MAUR****EZ",
//                        color = Color.Black,
//                        fontSize = 16.sp
//                    )
//                }
//            }
//        }