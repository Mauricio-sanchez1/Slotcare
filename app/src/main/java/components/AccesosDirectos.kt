package components

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.slotcare0011.ui.theme.SlotCare0011Theme
import screens.InfoAcceso


@Composable
fun AccesosDirectosCard(
    modifier: Modifier = Modifier,
    info: InfoAcceso, // <--- CAMBIO PRINCIPAL: Recibe un objeto InfoAcceso
    applyBlur: Boolean = true // Mantienes tu parámetro de blur
) {
    val cardShape = RoundedCornerShape(12.dp)

    Box(
        modifier = modifier
            .size(width = 120.dp, height = 100.dp) // Tu tamaño original
            .clip(cardShape)
            .clickable(onClick = info.accion) // <--- Usa info.accion de la data class
    ) {
        // Tu lógica de Blur y fondo (se mantiene igual)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (applyBlur && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        Modifier.blur(
                            radius = 15.dp,
                            edgeTreatment = BlurredEdgeTreatment.Unbounded
                        )
                    } else {
                        Modifier
                    }
                )
                .background(Color.DarkGray.copy(alpha = 0.25f))
        )
        // Contenido de la tarjeta (se mantiene igual, pero usa 'info.')
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = info.imagenResId), // <--- Usa info.imagenResId
                modifier = Modifier
                    .size(70.dp) // Tu tamaño original
                    .padding(bottom = 8.dp),
                contentDescription = "${info.titulo} icono" // <--- Usa info.titulo
            )
            Text(
                text = info.titulo, // <--- Usa info.titulo
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface // <--- COMPLETA LA LÍNEA ANTERIOR
            )
        }
    }
}

@Preview(showBackground = true, name = "Accesos Directos Card")
@Composable
fun AccesosDirectosCardPreview() {
    SlotCare0011Theme {
        // Para la preview, necesitamos crear un InfoAcceso de ejemplo
        val ejemploInfo = InfoAcceso(
            titulo = "Ejemplo",
            imagenResId = R.drawable.ic_launcher_foreground, // Usa un drawable de placeholder
            accion = {}
        )
        AccesosDirectosCard(
            info = ejemploInfo // Pasamos el objeto InfoAcceso
        )
    }
}