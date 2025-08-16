package com.quantumblocks.game.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.quantumblocks.game.R
import com.quantumblocks.game.ui.theme.GameButtonStyles
import com.quantumblocks.game.ui.theme.GameColors
import com.quantumblocks.game.ui.theme.GameSpacing
import com.quantumblocks.game.ui.theme.GameTextStyles
import com.quantumblocks.game.ui.theme.GameTypography

/**
 * Composable that displays the game control panel with action buttons
 */
@Composable
fun ControlPanel(
    onMoveLeft: () -> Unit,
    onMoveRight: () -> Unit,
    onRotate: () -> Unit,
    onDrop: () -> Unit,
    onDropStart: () -> Unit,
    onDropEnd: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(GameSpacing.ControlPanelHeight)
            .padding(GameSpacing.ExtraLarge),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Move Left Button
        GameButton(
            text = stringResource(R.string.move_left),
            onClick = onMoveLeft,
            modifier = Modifier.weight(1f)
        )
        
        // Move Right Button
        GameButton(
            text = stringResource(R.string.move_right),
            onClick = onMoveRight,
            modifier = Modifier.weight(1f)
        )
        
        // Rotate Button
        GameButton(
            text = stringResource(R.string.rotate),
            onClick = onRotate,
            modifier = Modifier.weight(1f)
        )
        
        // Drop Button
        GameButton(
            text = stringResource(R.string.drop),
            onClick = onDrop,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Custom button component for game controls
 */
@Composable
private fun GameButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier.padding(horizontal = GameSpacing.Small),
        colors = GameButtonStyles.primaryButtonColors(),
        elevation = GameButtonStyles.primaryElevation()
    ) {
        GameTextStyles.titleMedium(text = text)
    }
}

/**
 * Preview for ControlPanel composable
 */
@Preview(showBackground = true)
@Composable
fun ControlPanelPreview() {
    ControlPanel(
        onMoveLeft = {},
        onMoveRight = {},
        onRotate = {},
        onDrop = {},
        onDropStart = {},
        onDropEnd = {}
    )
}
