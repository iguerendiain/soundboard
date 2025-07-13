package nacholab.soundboard.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    background = VeryDarkGray,
    onBackground = White,

    primary = LLABlue,
    onPrimary = White,

    surface = LLABlue,
    onSurface = White,

    secondary = LLAYellow,
    onSecondary = LLABlue,

    tertiary = VeryDarkGray,
    onTertiary = White,
)

private val LightColorScheme = lightColorScheme(
    background = White,
    onBackground = Black,

    primary = LLABlue,
    onPrimary = White,

    surface = LLABlue,
    onSurface = White,

    secondary = LLAYellow,
    onSecondary = LLABlue,

    tertiary = White,
    onTertiary = Black,
)

@Composable
fun NachoLabsSoundBoardTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}