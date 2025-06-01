package screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.slotcare0011.R
import com.example.slotcare0011.ui.theme.SlotCare0011Theme


// Función composable que representa la pantalla de bienvenida
@Composable
fun BienvenidaScreen(
    onLogin: () -> Unit,           // Callback para el evento de inicio de sesión
    onNavigateToServicios: () -> Unit // Callback para navegar a servicios
) {
    // Contenedor principal que ocupa toda la pantalla
    Box(modifier = Modifier.fillMaxSize()) {

        /************************************************
         * SECCIÓN DE FONDO
         ************************************************/
        // Imagen de fondo que cubre toda la pantalla
        Image(
            painter = painterResource(id = R.drawable.background_3), // Recurso de imagen
            contentDescription = "Fondo decorativo de la app",       // Texto para accesibilidad
            modifier = Modifier.fillMaxSize(),                       // Ocupa todo el espacio
            contentScale = ContentScale.Crop                         // Recorta la imagen para cubrir
        )

        /************************************************
         * SECCIÓN DEL LOGOTIPO
         ************************************************/
        Image(
            painter = painterResource(R.drawable.logoslotcarepng),  // Recurso del logo
            contentDescription = "Logotipo de Slot Care",            // Descripción para lectores de pantalla
            modifier = Modifier
                .align(Alignment.TopCenter)  // Posiciona en centro superior
                .padding(top = 16.dp)        // Margen superior de 16dp
        )

        // Color constante para los textos (evita recrear el objeto Color)
        val textColor = Color.White

        /************************************************
         * SECCIÓN DE TEXTO CENTRAL
         ************************************************/
        Column(
            modifier = Modifier
                .align(Alignment.Center)     // Centra vertical y horizontalmente
                .padding(16.dp),            // Margen interno de 16dp
            horizontalAlignment = Alignment.CenterHorizontally, // Alineación horizontal
            verticalArrangement = Arrangement.Center // Alineación vertical
        ) {
            // Texto principal de bienvenida
            Text(
                text = "Bienvenido",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold // Texto en negrita
                ),
                color = textColor // Color blanco definido anteriormente
            )

            // Espaciador vertical de 16dp
            Spacer(modifier = Modifier.height(16.dp))

            // Texto secundario
            Text(
                text = "Inicia sesión para continuar",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold // Mantiene consistencia visual
                ),
                color = textColor
            )
        }

        /************************************************
         * SECCIÓN DE BOTONES INFERIORES
         ************************************************/
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter) // Posiciona en la parte inferior
                .fillMaxWidth()               // Ocupa todo el ancho
                .padding(                       // Margen interno:
                    bottom = 16.dp,            // 16dp en la base
                    start = 16.dp,             // 16dp a la izquierda
                    end = 16.dp               // 16dp a la derecha
                ),
            horizontalAlignment = Alignment.CenterHorizontally, // Centra hijos horizontalmente
            verticalArrangement = Arrangement.spacedBy(16.dp) // Espacio vertical entre elementos
        ) {
            /************************************************
             * BOTÓN DE INICIO DE SESIÓN
             ************************************************/
            Button(
                onClick = onLogin, // Acción al hacer clic
                modifier = Modifier
                    .fillMaxWidth()  // Ancho completo
                    .height(48.dp)  // Altura fija de 48dp (estándar Material Design)
                    .background(     // Fondo con gradiente
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFF97316), // Naranja (arriba)
                                Color(0xFFFFD700)  // Amarillo (abajo)
                            ),
                            startY = 0f,           // Inicio del gradiente (parte superior)
                            endY = Float.POSITIVE_INFINITY // Extender hasta el final
                        ),
                        shape = RoundedCornerShape(24.dp) // Forma de píldora
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent, // Fondo transparente para mostrar gradiente
                    contentColor = Color.White          // Color del texto
                ),
                shape = RoundedCornerShape(24.dp),     // Forma consistente
                elevation = null  // Elimina sombra para diseño plano
            ) {
                Text(
                    text = "Iniciar sesión",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold, // Texto en negrita
                        letterSpacing = 0.5.sp       // Espaciado entre letras
                    )
                )
            }

            /************************************************
             * BOTÓN SECUNDARIO
             ************************************************/
            Button(
                onClick = onNavigateToServicios,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFFFD700), // Amarillo (arriba)
                                Color(0xFFF97316)  // Naranja (abajo) - Orden invertido
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
                    text = "Continuar sin sesión", // Texto modificado para claridad
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                )
            }
        }
    }
}


@Preview(
    name = "Bienvenida - Modo Claro",
    showBackground = true
)
@Preview(
    name = "Bienvenida - Modo Nocturno",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES // Activa el modo nocturno
)
@Composable
fun BienvenidaScreenPreview() {
    SlotCare0011Theme { // Tu tema debería manejar los colores para modo claro/oscuro
        BienvenidaScreen(onLogin = {}, onNavigateToServicios = {})
    }
}

