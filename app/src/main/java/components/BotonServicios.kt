package components

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.slotcare0011.R
import com.example.slotcare0011.ui.theme.SlotCare0011Theme // Asegúrate que esta ruta sea correcta

@Composable
fun ServicioCard(
    modifier: Modifier = Modifier,
    titulo: String,
    @DrawableRes imagenResId: Int,
    onClick: () -> Unit,
    applyBlur: Boolean = true
) {
    val cardShape = RoundedCornerShape(12.dp)

    Box(
        modifier = modifier
            .size(width = 150.dp, height = 180.dp)
            .clip(cardShape)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize() // Ocupa todo el espacio del contenedor padre
                .then(
                    if (applyBlur && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        Modifier.blur(
                            radius = 15.dp, // Intensidad del desenfoque
                            // Esta edgeTreatment se aplica al blur mismo, no al contenido
                            edgeTreatment = BlurredEdgeTreatment.Unbounded // O cardShape, experimenta
                        )
                    } else {
                        Modifier
                    }
                )
                .background(Color.White.copy(alpha = 0.50f))
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp), // Padding interno para el contenido
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = imagenResId),
                modifier = Modifier
                    .size(70.dp)
                    .padding(bottom = 8.dp),
                contentDescription = "$titulo icono"
            )
            Text(
                text = titulo,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface // Color que contraste sobre el "cristal"
            )
        }
    }
}

// --- Composable principal para mostrar la cuadrícula de Botones de Servicios ---
// (BotonServicios y las Previews se mantienen igual que en la respuesta anterior,
//  asegúrate de tener una imagen de fondo en R.drawable.tu_imagen_de_fondo para BotonServicios)

@Composable
fun BotonServicios(
    modifier: Modifier = Modifier,
    onRepararPcClick: () -> Unit,
    onNuevaOrdenClick: () -> Unit,
    onRepararCpuClick: () -> Unit,
    onAddDispositivoClick: () -> Unit,
    onHistorialClick: () -> Unit,
    onConsultaTecnicaClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val applyCardBlur = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

            // Filas de ServicioCard...
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ServicioCard(
                    titulo = "Nueva Orden",
                    imagenResId = R.drawable.add_orden,
                    onClick = onNuevaOrdenClick,
                    applyBlur = applyCardBlur
                )
                ServicioCard(
                    titulo = "Nuevo Tecnico",
                    imagenResId = R.drawable.agregar_tecnico,
                    onClick = onRepararPcClick,
                    applyBlur = applyCardBlur
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ServicioCard(
                    titulo = "Reparación CPU",
                    imagenResId = R.drawable.reparar_cpu,
                    onClick = onRepararCpuClick,
                    applyBlur = applyCardBlur
                )
                ServicioCard(
                    titulo = "Add Dispositivo",
                    imagenResId = R.drawable.agregar_dispositivo,
                    onClick = onAddDispositivoClick,
                    applyBlur = applyCardBlur
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ServicioCard(
                    titulo = "Historial",
                    imagenResId = R.drawable.nueva_orden,
                    onClick = onHistorialClick,
                    applyBlur = applyCardBlur
                )
                ServicioCard(
                    titulo = "Consulta Técnica",
                    imagenResId = R.drawable.consulta,
                    onClick = onConsultaTecnicaClick,
                    applyBlur = applyCardBlur
                )
            }
        }
    }
}


@Preview(showBackground = true, name = "BotonServicios Light (Aero Intento 2)")
@Composable
fun BotonServiciosPreviewLight() {
    SlotCare0011Theme(darkTheme = false) {
        Box(Modifier.background(Color.LightGray)) {
            BotonServicios(
                onRepararPcClick = { },
                onNuevaOrdenClick = { },
                onRepararCpuClick = { },
                onAddDispositivoClick = { },
                onHistorialClick = { },
                onConsultaTecnicaClick = { }
            )
        }
    }
}

@Preview(showBackground = true, name = "BotonServicios Dark (Aero Intento 2)")
@Composable
fun BotonServiciosPreviewDark() {
    SlotCare0011Theme(darkTheme = true) {
        Box(Modifier.background(Color.DarkGray)) {
            BotonServicios(
                onRepararPcClick = { },
                onNuevaOrdenClick = { },
                onRepararCpuClick = { },
                onAddDispositivoClick = { },
                onHistorialClick = { },
                onConsultaTecnicaClick = { }
            )
        }
    }
}