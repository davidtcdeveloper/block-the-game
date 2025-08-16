package com.quantumblocks.game.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.quantumblocks.game.model.GameState
import com.quantumblocks.game.model.Piece
import com.quantumblocks.game.model.Position

/**
 * Comprehensive preview showing all game components together
 */
@Preview(showBackground = true, name = "Game Components - Active Game")
@Composable
fun GameComponentsActivePreview() {
    // Create a game state with some blocks and current piece
    val boardWithBlocks = List(20) { row ->
        List(10) { col ->
            // Add some blocks in the bottom rows
            row >= 18 && col in 3..6
        }
    }
    
    val gameState = GameState(
        board = boardWithBlocks,
        currentPiece = Piece.createTPiece().move(Position(5, 3)),
        score = 1250,
        level = 3
    )
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Score Display
            ScoreDisplay(
                score = gameState.score,
                level = gameState.level
            )
            
            // Game Board
            GameBoard(
                gameState = gameState,
                modifier = Modifier.weight(1f)
            )
            
            // Control Panel
            ControlPanel(
                onMoveLeft = {},
                onMoveRight = {},
                onRotate = {},
                onDrop = {},
                onDropStart = {},
                onDropEnd = {}
            )
        }
    }
}

/**
 * Preview showing game over state with dialog
 */
@Preview(showBackground = true, name = "Game Components - Game Over")
@Composable
fun GameComponentsGameOverPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Score Display
            ScoreDisplay(
                score = 8750,
                level = 9
            )
            
            // Game Board (empty for game over)
            GameBoard(
                gameState = GameState(
                    score = 8750,
                    level = 9,
                    gameOver = true
                ),
                modifier = Modifier.weight(1f)
            )
            
            // Control Panel
            ControlPanel(
                onMoveLeft = {},
                onMoveRight = {},
                onRotate = {},
                onDrop = {},
                onDropStart = {},
                onDropEnd = {}
            )
        }
        
        // Game Over Dialog (overlay)
        GameOverDialog(
            score = 8750,
            level = 9,
            onRestart = {}
        )
    }
}

/**
 * Preview showing individual components in a grid layout
 */
@Preview(showBackground = true, name = "Individual Components")
@Composable
fun IndividualComponentsPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Score Display
            ScoreDisplay(
                score = 150,
                level = 1
            )
            
            // Game Board (smaller size for preview)
            GameBoard(
                gameState = GameState(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            
            // Control Panel
            ControlPanel(
                onMoveLeft = {},
                onMoveRight = {},
                onRotate = {},
                onDrop = {},
                onDropStart = {},
                onDropEnd = {}
            )
        }
    }
}
