package com.quantumblocks.game.model

import androidx.compose.runtime.Immutable

/**
 * Represents the current state of the game
 */
@Immutable
data class GameState(
    val board: List<List<Boolean>> = List(20) { List(10) { false } },
    val currentPiece: Piece? = null, // Can be null before the first piece or if game over immediately
    val gameOver: Boolean = false,
    val score: Int = 0,
    val level: Int = 1,
    val needsNewPiece: Boolean = false, // Flag to indicate a new piece should be spawned
    val softDropActive: Boolean = false, // Flag to indicate soft drop is active
) {
    val boardHeight: Int get() = board.size
    val boardWidth: Int get() = board[0].size

    fun isPositionValid(position: Position): Boolean =
        position.row >= 0 &&
            position.row < boardHeight &&
            position.col >= 0 &&
            position.col < boardWidth &&
            !board[position.row][position.col]

    // Check if a piece can be placed on the current board (doesn't overlap with existing blocks)
    // This is different from isPositionValid, which checks a single board cell.
    fun canPlacePiece(piece: Piece): Boolean = piece.blocks.all { isPositionValid(it) }

    fun placePiece(pieceToPlace: Piece): GameState {
        val newBoard = board.map { it.toMutableList() }.toMutableList()
        pieceToPlace.blocks.forEach { pos ->
            if (pos.row >= 0 && pos.row < boardHeight && pos.col >= 0 && pos.col < boardWidth) {
                newBoard[pos.row][pos.col] = true
            }
        }
        // After placing, there is no current piece floating, and we signal that a new one is needed.
        return copy(board = newBoard.map { it.toList() }, currentPiece = null, needsNewPiece = true)
    }

    fun clearLines(): GameState {
        val linesToClearIndices =
            board.mapIndexedNotNull { index, row ->
                if (row.all { it }) index else null
            }

        if (linesToClearIndices.isEmpty()) {
            return this
        }

        val newBoard = board.toMutableList()
        linesToClearIndices.sortedDescending().forEach { lineIndex ->
            // Iterate descending to avoid index issues
            newBoard.removeAt(lineIndex)
            newBoard.add(0, List(boardWidth) { false })
        }

        val linesClearedCount = linesToClearIndices.size
        val pointsEarned =
            when (linesClearedCount) {
                1 -> 100
                2 -> 300
                3 -> 500
                4 -> 800
                else -> 0 // Should not happen with standard tetris pieces
            } * level // Multiply by level after determining base points

        val newScore = score + pointsEarned
        // Level up every 1000 points, ensure level is at least 1
        val newLevel = (newScore / 1000) + 1

        return copy(
            board = newBoard.map { it.toList() }, // Ensure immutability
            score = newScore,
            level = newLevel,
        )
    }
}
