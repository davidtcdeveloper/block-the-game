package com.quantumblocks.game.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.quantumblocks.game.ui.components.ControlPanel
import com.quantumblocks.game.ui.components.GameBoard
import com.quantumblocks.game.ui.components.GameOverDialog
import com.quantumblocks.game.ui.components.ScoreDisplay
import com.quantumblocks.game.viewmodel.GameViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Main game screen that displays the game board, controls, and score
 */
@Composable
fun GameScreen(
    viewModel: GameViewModel = koinViewModel()
) {
    val gameState by viewModel.gameState.collectAsState()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Score Display at the top
            ScoreDisplay(
                score = gameState.score,
                level = gameState.level,
                modifier = Modifier.fillMaxWidth()
            )
            
            // Game Board in the center
            GameBoard(
                gameState = gameState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 16.dp)
            )
            
            // Control Panel at the bottom
            ControlPanel(
                onMoveLeft = { viewModel.onMoveLeft() },
                onMoveRight = { viewModel.onMoveRight() },
                onRotate = { viewModel.onRotate() },
                onDrop = { viewModel.onDrop() },
                onDropStart = { viewModel.onDropStart() },
                onDropEnd = { viewModel.onDropEnd() },
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        // Game Over Dialog
        if (gameState.gameOver) {
            GameOverDialog(
                score = gameState.score,
                level = gameState.level,
                onRestart = { viewModel.startNewGame() }
            )
        }
    }
}
