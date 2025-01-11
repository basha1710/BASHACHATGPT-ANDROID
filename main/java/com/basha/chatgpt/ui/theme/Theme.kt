package com.basha.chatgpt.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Define Colors

val ColorModelMessage = Color(0xFF4CAF50) // Green for model messages
val ColorUserMessage = Color(0xFF2196F3) // Blue for user messages

val Purple80 = Color(0xFF9C4DFF)
val PurpleGrey80 = Color(0xFF7C4DFF)
val Pink80 = Color(0xFFFF4081)

val Purple40 = Color(0xFF8A2BE2)
val PurpleGrey40 = Color(0xFF3F51B5)
val Pink40 = Color(0xFFEF5350)

@Composable
fun BashagptTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkColorScheme(
            primary = Purple80,
            secondary = PurpleGrey80,
            tertiary = Pink80
        )

        else -> lightColorScheme(
            primary = Purple40,
            secondary = PurpleGrey40,
            tertiary = Pink40
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}
