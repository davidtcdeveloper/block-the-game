package com.quantumblocks.game.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import com.quantumblocks.game.model.GameState
import com.quantumblocks.game.model.Piece
import com.quantumblocks.game.model.Position
import com.quantumblocks.game.ui.theme.GameColors
import com.quantumblocks.game.ui.theme.GameSpacing

/**
 * Composable that displays the game board with grid, locked blocks, and current piece
 */
@Composable
fun GameBoard(
    gameState: GameState,
    modifier: Modifier = Modifier,
) {
    val boardHeight = gameState.boardHeight
    val boardWidth = gameState.boardWidth

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .aspectRatio(boardWidth.toFloat() / boardHeight.toFloat())
                .background(GameColors.Black)
                .border(GameSpacing.BorderWidth, GameColors.Gray)
                .padding(GameSpacing.BorderWidth),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(boardWidth.toFloat() / boardHeight.toFloat()),
        ) {
            val cellWidth = size.width / boardWidth
            val cellHeight = size.height / boardHeight

            // Draw grid lines
            drawGrid(boardWidth, boardHeight, cellWidth, cellHeight)

            // Draw locked blocks
            drawLockedBlocks(gameState.board, cellWidth, cellHeight)

            // Draw current piece if it exists
            gameState.currentPiece?.let { piece ->
                drawPiece(piece, cellWidth, cellHeight)
            }
        }
    }
}

/**
 * Draws the grid lines on the canvas
 */
private fun DrawScope.drawGrid(
    boardWidth: Int,
    boardHeight: Int,
    cellWidth: Float,
    cellHeight: Float,
) {
    val gridColor = GameColors.GrayTransparent

    // Draw vertical lines
    for (col in 0..boardWidth) {
        val x = col * cellWidth
        drawLine(
            color = gridColor,
            start = Offset(x, 0f),
            end = Offset(x, size.height),
            strokeWidth = 1f,
        )
    }

    // Draw horizontal lines
    for (row in 0..boardHeight) {
        val y = row * cellHeight
        drawLine(
            color = gridColor,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = 1f,
        )
    }
}

/**
 * Draws the locked blocks on the board
 */
private fun DrawScope.drawLockedBlocks(
    board: List<List<Boolean>>,
    cellWidth: Float,
    cellHeight: Float,
) {
    // For now, locked blocks are a single color as per previous iterations.
    val blockColor = GameColors.Blue

    board.forEachIndexed { row, rowData ->
        rowData.forEachIndexed { col, hasBlock ->
            if (hasBlock) {
                val x = col * cellWidth
                val y = row * cellHeight

                drawRect(
                    color = blockColor,
                    topLeft = Offset(x, y),
                    size = Size(cellWidth, cellHeight),
                )

                // Draw block border
                drawRect(
                    color = GameColors.White,
                    topLeft = Offset(x, y),
                    size = Size(cellWidth, cellHeight),
                    style = Stroke(width = 1f),
                )
            }
        }
    }
}

/**
 * Draws the current falling piece
 */
private fun DrawScope.drawPiece(
    piece: com.quantumblocks.game.model.Piece,
    cellWidth: Float,
    cellHeight: Float,
) {
    val pieceColor = piece.color

    piece.blocks.forEach { position ->
        val x = position.col * cellWidth
        val y = position.row * cellHeight

        // Only draw if the block is visible on the board
        if (position.row >= 0 && position.col >= 0) {
            drawRect(
                color = pieceColor,
                topLeft = Offset(x, y),
                size = Size(cellWidth, cellHeight),
            )

            // Draw piece border
            drawRect(
                color = GameColors.White, // Consider a contrasting border or remove if colors are distinct enough
                topLeft = Offset(x, y),
                size = Size(cellWidth, cellHeight),
                style = Stroke(width = 2f),
            )
        }
    }
}

/**
 * Preview for GameBoard composable with empty board
 */
@Preview(showBackground = true)
@Composable
fun GameBoardEmptyPreview() {
    GameBoard(
        gameState = GameState(),
    )
}

/**
 * Preview for GameBoard composable with some blocks and current piece
 */
@Preview(showBackground = true)
@Composable
fun GameBoardWithBlocksPreview() {
    // Create a game state with some locked blocks
    val boardWithBlocks =
        List(20) { row ->
            List(10) { col ->
                // Add some blocks in the bottom rows
                row >= 18 && col in 3..6
            }
        }

    // Create a sample piece for previewing
    val samplePiece = Piece.createTPiece().move(Position(5, 3))

    val gameState =
        GameState(
            board = boardWithBlocks,
            currentPiece = samplePiece,
        )

    GameBoard(gameState = gameState)
}
