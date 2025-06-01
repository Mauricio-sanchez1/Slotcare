package com.example.slotcare0011.ui.theme


import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat


val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),       // Esta es la que OutlinedTextField usará por defecto
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(33.dp)
)

// --- Estructuras para Colores Extendidos ---
@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

// Valor por defecto para ColorFamily, útil para el CompositionLocal
val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

@Immutable
data class ExtendedColorScheme(
    val customColor1: ColorFamily
    // Puedes añadir más colores personalizados aquí si es necesario
    // val customColor2: ColorFamily = unspecified_scheme
)

// CompositionLocal para proveer el ExtendedColorScheme
val LocalExtendedColorScheme = staticCompositionLocalOf {
    ExtendedColorScheme(
        customColor1 = unspecified_scheme // Valor por defecto inicial
    )
}

// Extensión de MaterialTheme para acceder fácilmente al ExtendedColorScheme
val MaterialTheme.extendedColorScheme: ExtendedColorScheme
    @Composable
    @ReadOnlyComposable
    get() = LocalExtendedColorScheme.current

// --- Definiciones de ColorScheme para el tema base (usando colores de tu Color.kt) ---

private val lightScheme = lightColorScheme(

    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

// Las paletas de contraste están definidas pero no se usan activamente para seleccionar
// el ColorScheme base ni el ExtendedColorScheme en la función AppTheme,
// a menos que añadas lógica específica para ello.
// private val mediumContrastLightColorScheme = lightColorScheme(...)
// private val highContrastLightColorScheme = lightColorScheme(...)
// private val mediumContrastDarkColorScheme = darkColorScheme(...)
// private val highContrastDarkColorScheme = darkColorScheme(...)


// --- Definiciones de ExtendedColorScheme para claro y oscuro ---
// Aquí asignamos los roles de 'tertiary' a 'customColor1' como ejemplo.
// ¡CAMBIA ESTO SI QUIERES QUE customColor1 USE OTROS COLORES DE TU Color.kt!
val extendedLight = ExtendedColorScheme(
    customColor1 = ColorFamily(
        color = tertiaryLight,                 // Ejemplo: usa tertiaryLight como el color principal de customColor1
        onColor = onTertiaryLight,             // Ejemplo: usa onTertiaryLight
        colorContainer = tertiaryContainerLight, // Ejemplo: usa tertiaryContainerLight
        onColorContainer = onTertiaryContainerLight // Ejemplo: usa onTertiaryContainerLight
    ),
)

val extendedDark = ExtendedColorScheme(
    customColor1 = ColorFamily(
        color = tertiaryDark,                 // Ejemplo: usa tertiaryDark
        onColor = onTertiaryDark,             // Ejemplo: usa onTertiaryDark
        colorContainer = tertiaryContainerDark, // Ejemplo: usa tertiaryContainerDark
        onColorContainer = onTertiaryContainerDark // Ejemplo: usa onTertiaryContainerDark
    ),
)

// (Opcional) Instancias extendidas para contraste, si decides usarlas más adelante
// y defines cómo deben mapearse. Por ahora, no se usan.
// val extendedLightMediumContrast = ExtendedColorScheme(...)
// val extendedLightHighContrast = ExtendedColorScheme(...)
// val extendedDarkMediumContrast = ExtendedColorScheme(...)
// val extendedDarkHighContrast = ExtendedColorScheme(...)


// --- Composable Principal del Tema ---

@Composable
fun SlotCare0011Theme( // Puedes renombrar esto a SlotCare0011Theme si es tu tema principal
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true, // Dynamic color is disponible en Android 12+
    content: @Composable () -> Unit
) {
    // 1. Determinar el ColorScheme base (claro, oscuro o dinámico)
    val baseColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> darkScheme
        else -> lightScheme
    }

    // 2. Determinar el ExtendedColorScheme apropiado (claro u oscuro)
    //    Si necesitas que los colores extendidos también respeten el contraste,
    //    necesitarías una lógica más compleja aquí o pasar un parámetro de contraste al tema.
    val currentExtendedColorScheme = if (darkTheme) extendedDark else extendedLight

    // 3. Configurar la barra de estado del sistema (opcional pero bueno tenerlo)
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = baseColorScheme.primary.toArgb() // O el color que prefieras
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    // 4. Proveer el ExtendedColorScheme y aplicar el MaterialTheme
    CompositionLocalProvider(LocalExtendedColorScheme provides currentExtendedColorScheme) {
        MaterialTheme(
            colorScheme = baseColorScheme,
            typography = Typography, // Asegúrate de que 'Typography' esté definida en Type.kt
            content = content,
            shapes = AppShapes
        )
    }
}