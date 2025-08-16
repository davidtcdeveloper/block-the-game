package com.quantumblocks.game.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.quantumblocks.game.R
import com.quantumblocks.game.ui.theme.GameColors
import com.quantumblocks.game.ui.theme.GameModifiers
import com.quantumblocks.game.ui.theme.GameSpacing
import com.quantumblocks.game.ui.theme.GameTextStyles

/**
 * Composable that displays the current score and level
 */
@Composable
fun ScoreDisplay(
    score: Int,
    level: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(GameSpacing.ExtraLarge),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Score Display
        ScoreCard(
            title = stringResource(R.string.score),
            value = score.toString(),
            modifier = Modifier.weight(1f)
        )
        
        // Level Display
        ScoreCard(
            title = stringResource(R.string.level),
            value = level.toString(),
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Individual score/level card component
 */
@Composable
private fun ScoreCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .then(GameModifiers.cardBackground())
            .padding(GameSpacing.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        GameTextStyles.bodySmall(text = title)
        GameTextStyles.titleMedium(text = value)
    }
}

/**
 * Preview for ScoreDisplay composable with low score
 */
@Preview(showBackground = true)
@Composable
fun ScoreDisplayLowScorePreview() {
    ScoreDisplay(
        score = 150,
        level = 1
    )
}

/**
 * Preview for ScoreDisplay composable with high score
 */
@Preview(showBackground = true)
@Composable
fun ScoreDisplayHighScorePreview() {
    ScoreDisplay(
        score = 8750,
        level = 9
    )
}
