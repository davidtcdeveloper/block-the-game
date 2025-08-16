package com.quantumblocks.game.model

import androidx.compose.runtime.Immutable

/**
 * Represents a tetromino piece with its blocks and rotation state
 */
@Immutable
data class Piece(
    val blocks: List<Position>,
    val center: Position = Position(0, 0)
) {
    fun rotate(): Piece {
        val rotatedBlocks = blocks.map { pos ->
            val relativePos = pos - center
            val rotatedRelative = Position(-relativePos.col, relativePos.row)
            center + rotatedRelative
        }
        return copy(blocks = rotatedBlocks)
    }

    fun move(direction: Position): Piece {
        val movedBlocks = blocks.map { it + direction }
        return copy(blocks = movedBlocks, center = center + direction)
    }

    companion object {
        // T-piece tetromino
        fun createTPiece(): Piece {
            return Piece(
                blocks = listOf(
                    Position(0, 1),  // Top
                    Position(1, 0),  // Left
                    Position(1, 1),  // Center
                    Position(1, 2)   // Right
                ),
                center = Position(1, 1)
            )
        }
    }
}
