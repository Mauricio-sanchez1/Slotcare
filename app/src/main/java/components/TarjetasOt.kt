package components // O tu paquete correcto

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.slotcare0011.ui.theme.SlotCare0011Theme // Asegúrate que la ruta a tu tema sea correcta

/// --- Definiciones de Colores ---
val ColorEstadoActiva = Color(0xFF4CAF50)       // Verde (Para Activa)
val ColorEstadoEnProceso = Color(0xFF2196F3)    // Azul (Si quieres diferenciarlo) o Amarillo si es igual a Activa
val ColorEstadoFinalizada = Color(0xFF9E9E9E)   // Gris (Antes era verde, ajusta si es necesario)
val ColorEstadoNoAsignada = Color(0xFFF44336)   // Rojo
val ColorEstadoPendiente = Color(0xFFFFC107)    // Amarillo (Nuevo para Pendiente si lo necesitas)
val ColorEstadoCancelada = Color(0xFFB00020)    // Rojo Oscuro (Nuevo para Cancelada si lo necesitas)
val ColorEstadoDesconocido = Color.Gray

enum class EstadoOrden(val displayName: String, val color: Color) {
    ACTIVA("Activa", ColorEstadoActiva),                      // <-- AÑADIDO
    EN_PROCESO("En Proceso", ColorEstadoEnProceso),
    PENDIENTE("Pendiente", ColorEstadoPendiente),              // <-- AÑADIDO (Opcional, si lo usas)
    NO_ASIGNADA("No Asignada", ColorEstadoNoAsignada),
    FINALIZADA("Finalizada", ColorEstadoFinalizada),
    CANCELADA("Cancelada", ColorEstadoCancelada),            // <-- AÑADIDO (Opcional, si lo usas)
    DESCONOCIDO("Desconocido", ColorEstadoDesconocido);

    companion object {
        fun fromString(statusString: String?): EstadoOrden {
            return when (statusString?.trim()?.lowercase()) {
                "activa" -> ACTIVA                               // <-- CAMBIADO
                "en proceso", "enproceso" -> EN_PROCESO
                "pendiente" -> PENDIENTE                         // <-- CAMBIADO/AÑADIDO
                "no asignada", "noasignada" -> NO_ASIGNADA
                "finalizada" -> FINALIZADA
                "cancelada" -> CANCELADA                       // <-- AÑADIDO
                else -> DESCONOCIDO
            }
        }
    }
}


@Composable
fun OrderCard(
    modifier: Modifier = Modifier,
    numeroOrden: String,
    asignadoA: String?,
    ubicacion: String,
    estadoString: String,
    onClick: (orderId: String) -> Unit // NUEVO: Lambda para el clic, pasa el ID
) {
    val estadoOrden = EstadoOrden.fromString(estadoString)

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 12.dp)
            .clickable { onClick(numeroOrden) }, // ¡NUEVO! Hacer la tarjeta clickeable
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // ... (contenido interno de la tarjeta como antes) ...
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "No. Trabajo: $numeroOrden",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(estadoOrden.color)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            InfoRow(label = "Asignado a:", value = asignadoA ?: "N/A")
            InfoRow(label = "Ubicación:", value = ubicacion)

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Estado: ${estadoOrden.displayName}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = estadoOrden.color
            )
        }
    }
}
@Composable
fun InfoRow(label: String, value: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically // Opcional, ajusta según necesites
    ) {
        Text(
            text = "$label ", // Añade un espacio si quieres
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold, // Opcional
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

// --- PREVIEWS (incluyendo la de Desconocido) ---

@Preview(showBackground = true, backgroundColor = 0xFFF0F0F0)
@Composable
fun OrderCardPreviewFinalizada() {
    SlotCare0011Theme {
        OrderCard(
            numeroOrden = "OT-2023-001",
            asignadoA = "Juan Pérez",
            ubicacion = "Oficina A-101",
            estadoString = "Finalizada",
            onClick = { orderId -> println("Preview Card clicked: $orderId") } // <--- AÑADIDO
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F0F0)
@Composable
fun OrderCardPreviewEnProceso() {
    SlotCare0011Theme {
        OrderCard(
            numeroOrden = "OT-2023-002",
            asignadoA = "Ana López",
            ubicacion = "Laboratorio B",
            estadoString = "En Proceso",
            onClick = { orderId -> println("Preview Card clicked: $orderId") } // <--- AÑADIDO
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F0F0)
@Composable
fun OrderCardPreviewNoAsignada() {
    SlotCare0011Theme {
        OrderCard(
            numeroOrden = "OT-2023-003",
            asignadoA = null,
            ubicacion = "Recepción",
            estadoString = "No Asignada",
            onClick = { orderId -> println("Preview Card clicked: $orderId") } // <--- AÑADIDO
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F0F0, name = "Order Card Desconocido")
@Composable
fun OrderCardPreviewDesconocido() {
    SlotCare0011Theme {
        OrderCard(
            numeroOrden = "OT-2023-004",
            asignadoA = "Carlos Ruiz",
            ubicacion = "Almacén",
            estadoString = "inventado",
            onClick = { orderId -> println("Preview Card clicked: $orderId") } // <--- AÑADIDO
        )
    }
}
@Preview(showBackground = true, backgroundColor = 0xFFF0F0F0)
@Composable
fun OrderCardPreviewActiva() { // Nueva preview o modifica una existente
    SlotCare0011Theme {
        OrderCard(
            numeroOrden = "OT-2023-005",
            asignadoA = "Maria Sol",
            ubicacion = "Taller",
            estadoString = "Activa", // Prueba el nuevo estado
            onClick = { /* ... */ }
        )
    }
}