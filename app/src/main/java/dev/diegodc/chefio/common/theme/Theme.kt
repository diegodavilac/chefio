package dev.diegodc.chefio.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController


private val DarkColorPalette = darkColors(
    primary = PRIMARY_COLOR,
    primaryVariant = PRIMARY_VARIANT_COLOR,
    secondary =  SECONDARY_COLOR
)
private val LightColorPalette = lightColors(
    primary = PRIMARY_COLOR,
    primaryVariant = PRIMARY_VARIANT_COLOR,
    secondary = SECONDARY_COLOR,
)

@Composable
fun ChefIOTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = PRIMARY_VARIANT_COLOR,
        darkIcons = false
    )

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}