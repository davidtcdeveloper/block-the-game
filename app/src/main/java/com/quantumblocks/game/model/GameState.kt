package com.quantumblocks.game.model

import androidx.compose.runtime.Immutable

/**
 * Represents the current state of the game
 */
@Immutable
data class GameState(
    val board: List<List<Boolean>> = List(20) { List(10) { false } },
    val currentPiece: Piece = Piece.createTPiece().move(Position(0, 5)),
    val gameOver: Boolean = false,
    val score: Int = 0,
    val level: Int = 1
) {
    val boardHeight: Int get() = board.size
    val boardWidth: Int get() = board[0].size

    fun isPositionValid(position: Position): Boolean {
        return position.row >= 0 &&
                position.row < boardHeight &&
                position.col >= 0 &&
                position.col < boardWidth &&
                !board[position.row][position.col]
    }

    fun canPlacePiece(piece: Piece): Boolean {
        return piece.blocks.all { isPositionValid(it) }
    }

    fun placePiece(piece: Piece): GameState {
        val newBoard = board.map { it.toMutableList() }.toMutableList()
        piece.blocks.forEach { pos ->
            if (pos.row >= 0 && pos.row < boardHeight && pos.col >= 0 && pos.col < boardWidth) {
                newBoard[pos.row][pos.col] = true
            }
        }
        return copy(board = newBoard.map { it.toList() })
    }

    fun clearLines(): GameState {
        val linesToClear = board.mapIndexedNotNull { index, row ->
            if (row.all { it }) {
                index
            } else {
                null
            }
        }

        if (linesToClear.isEmpty()) {
            return this
        }

        val newBoard = board.toMutableList()
        linesToClear.forEach { lineIndex ->
            newBoard.removeAt(lineIndex)
            newBoard.add(0, List(boardWidth) { false })
        }

        val newScore = score + (linesToClear.size * 100 * level)
        val newLevel = (newScore / 1000) + 1

        return copy(
            board = newBoard,
            score = newScore,
            level = newLevel
        )
    }
}
