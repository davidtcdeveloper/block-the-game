package com.quantumblocks.game.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
 * Composable that displays the game over dialog
 */
@Composable
fun GameOverDialog(
    score: Int,
    level: Int,
    onRestart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = { /* Do nothing - user must click restart */ },
        modifier = modifier,
        containerColor = GameColors.White,
        shape = RoundedCornerShape(GameSpacing.DialogCornerRadius),
        title = {
            GameTextStyles.titleLarge(
                text = stringResource(R.string.game_over),
                modifier = Modifier.fillMaxWidth(),
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(),
            ) {
                GameTextStyles.bodyLarge(
                    text = stringResource(R.string.final_score, score),
                )

                Spacer(modifier = Modifier.height(GameSpacing.Medium))

                GameTextStyles.bodyMedium(
                    text = stringResource(R.string.level_reached, level),
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onRestart,
                colors = GameButtonStyles.secondaryButtonColors(),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = GameSpacing.ExtraLarge),
            ) {
                GameTextStyles.bodyMedium(
                    text = stringResource(R.string.restart),
                    fontWeight = GameTypography.Bold,
                )
            }
        },
    )
}

/**
 * Preview for GameOverDialog composable
 */
@Preview(showBackground = true)
@Composable
fun GameOverDialogPreview() {
    GameOverDialog(
        score = 1250,
        level = 3,
        onRestart = {},
    )
}
