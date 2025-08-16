package com.quantumblocks.game.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Game color palette
 */
object GameColors {
    val DarkGray = Color.DarkGray
    val White = Color.White
    val Blue = Color.Blue
    val Black = Color.Black
    val Gray = Color.Gray
    val Cyan = Color.Cyan
    
    // Transparent colors
    val WhiteTransparent = Color.White.copy(alpha = 0.8f)
    val GrayTransparent = Color.Gray.copy(alpha = 0.3f)
}

/**
 * Game spacing values
 */
object GameSpacing {
    val Small = 4.dp
    val Medium = 8.dp
    val Large = 12.dp
    val ExtraLarge = 16.dp
    val ControlPanelHeight = 80.dp
    val CardCornerRadius = 8.dp
    val DialogCornerRadius = 16.dp
    val BorderWidth = 2.dp
}

/**
 * Game typography styles
 */
object GameTypography {
    val TitleLarge = 24.sp
    val TitleMedium = 20.sp
    val BodyLarge = 18.sp
    val BodyMedium = 16.sp
    val BodySmall = 14.sp
    
    val Bold = FontWeight.Bold
    val Medium = FontWeight.Medium
}

/**
 * Game button styles
 */
object GameButtonStyles {
    @Composable
    fun primaryButtonColors(): ButtonColors {
        return ButtonDefaults.buttonColors(
            containerColor = GameColors.DarkGray,
            contentColor = GameColors.White
        )
    }


    @Composable
    fun secondaryButtonColors(): ButtonColors {
        return ButtonDefaults.buttonColors(
            containerColor = GameColors.Blue,
            contentColor = GameColors.White
        )
    }

    @Composable
    fun primaryElevation() = ButtonDefaults.buttonElevation(
        defaultElevation = 4.dp,
        pressedElevation = 2.dp
    )
}

/**
 * Game text styles
 */
object GameTextStyles {
    @Composable
    fun titleLarge(
        text: String,
        modifier: Modifier = Modifier,
        textAlign: TextAlign = TextAlign.Center
    ) {
        Text(
            text = text,
            fontSize = GameTypography.TitleLarge,
            fontWeight = GameTypography.Bold,
            textAlign = textAlign,
            modifier = modifier
        )
    }

    @Composable
    fun titleMedium(
        text: String,
        modifier: Modifier = Modifier,
        textAlign: TextAlign = TextAlign.Center
    ) {
        Text(
            text = text,
            fontSize = GameTypography.TitleMedium,
            fontWeight = GameTypography.Bold,
            textAlign = textAlign,
            modifier = modifier
        )
    }
    
    @Composable
    fun bodyLarge(
        text: String,
        modifier: Modifier = Modifier,
        textAlign: TextAlign = TextAlign.Center,
        fontWeight: FontWeight = GameTypography.Medium
    ) {
        Text(
            text = text,
            fontSize = GameTypography.BodyLarge,
            fontWeight = fontWeight,
            textAlign = textAlign,
            modifier = modifier
        )
    }
    
    @Composable
    fun bodyMedium(
        text: String,
        modifier: Modifier = Modifier,
        textAlign: TextAlign = TextAlign.Center,
        fontWeight: FontWeight = FontWeight.Normal
    ) {
        Text(
            text = text,
            fontSize = GameTypography.BodyMedium,
            fontWeight = fontWeight,
            textAlign = textAlign,
            modifier = modifier
        )
    }
    
    @Composable
    fun bodySmall(
        text: String,
        modifier: Modifier = Modifier,
        textAlign: TextAlign = TextAlign.Center,
        color: Color = GameColors.WhiteTransparent
    ) {
        Text(
            text = text,
            fontSize = GameTypography.BodySmall,
            color = color,
            textAlign = textAlign,
            modifier = modifier
        )
    }
}

/**
 * Game modifier extensions
 */
object GameModifiers {
    @Composable
    fun cardBackground(): Modifier {
        return Modifier.background(
            color = GameColors.DarkGray,
            shape = RoundedCornerShape(GameSpacing.CardCornerRadius)
        )
    }
    
    @Composable
    fun dialogBackground(): Modifier {
        return Modifier.background(
            color = GameColors.White,
            shape = RoundedCornerShape(GameSpacing.DialogCornerRadius)
        )
    }
    
    @Composable
    fun gameBoardBorder(): Modifier {
        return Modifier.background(GameColors.Black)
    }
}
