package com.example.eduenglish.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(

    primary = Light_Primary,
    secondary = Light_Secondary, // MaterialTheme.colorScheme.secondary
    tertiary = Light_Tertiary,
    surface = Light_Text

)

private val DarkColorScheme = darkColorScheme(

    primary = Dark_Primary,
    secondary = Dark_Secondary,
    tertiary = Dark_Tertiary,
    surface = Dark_Text
)

private val GreenColorScheme = lightColorScheme(

    primary = Green_Primary,
    secondary = Green_Secondary,
    tertiary = Green_Tertiary,
    surface = Green_Text
)

@Composable
fun EduEnglishTheme(
    appTheme: AppTheme = AppTheme.WHITE,
    content: @Composable () -> Unit
) {

    val colorScheme = when (appTheme) {
        AppTheme.WHITE -> LightColorScheme
        AppTheme.DARK -> DarkColorScheme
        AppTheme.EXP -> GreenColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )

}