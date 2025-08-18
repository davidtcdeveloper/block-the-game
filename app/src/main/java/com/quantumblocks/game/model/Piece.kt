package com.quantumblocks.game.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Represents the different types of tetromino pieces.
 */
//TODO: Create a sealed class or similar
enum class PieceType {
    I, O, T, S, Z, J, L
}

/**
 * Represents a tetromino piece with its blocks, rotation state, type, and color.
 */
@Immutable
data class Piece(
    val type: PieceType,
    val blocks: List<Position>,
    /** The absolute board coordinates of the piece's rotation center. */
    val center: Position,
    val color: Color
) {
    /**
     * Rotates the piece 90 degrees clockwise around its center.
     * For the O piece, rotation does nothing.
     */
    //TODO: Extract this into a behavior or visitor
    fun rotate(): Piece {
        if (type == PieceType.O) return this // O piece doesn't rotate

        val rotatedBlocks = blocks.map { blockPosition ->
            // Calculate block position relative to the piece's center
            val relativePos = blockPosition - center
            // Perform clockwise rotation: (x, y) -> (y, -x)
            // Here, col is x, row is y. So, new_col_rel = relativePos.row, new_row_rel = -relativePos.col
            val rotatedRelativePos = Position(row = -relativePos.col, col = relativePos.row)
            // Translate back to absolute board coordinates
            center + rotatedRelativePos
        }
        return copy(blocks = rotatedBlocks)
    }

    fun move(direction: Position): Piece {
        val movedBlocks = blocks.map { it + direction }
        return copy(blocks = movedBlocks, center = center + direction)
    }

    //TODO: Move each function into the specific implementation
    companion object {
        // Define standard tetromino colors
        private val I_COLOR = Color(0xFF00FFFF) // Cyan
        private val O_COLOR = Color(0xFFFFFF00) // Yellow
        private val T_COLOR = Color(0xFF800080) // Purple
        private val S_COLOR = Color(0xFF00FF00) // Green
        private val Z_COLOR = Color(0xFFFF0000) // Red
        private val J_COLOR = Color(0xFF0000FF) // Blue
        private val L_COLOR = Color(0xFFFFA500) // Orange

        // Initial spawn offset for pieces, near top-center of a 10-wide board.
        // (row=0, col=4) means the piece's local (0,0) point will be at board (0,4).
        private val SPAWN_OFFSET = Position(0, 4)

        // Piece definitions use a local coordinate system for blocks and center.
        // These are then translated by SPAWN_OFFSET to get initial board positions.

        fun createIPiece(): Piece {
            // Local: Horizontal I-piece. X X X X on row 1. Pivot is the second X.
            // . . . .
            // X X X X
            // . . . .
            // . . . .
            val localBlocks = listOf(Position(1,0), Position(1,1), Position(1,2), Position(1,3))
            val localCenter = Position(1,1)
            return Piece(
                type = PieceType.I,
                blocks = localBlocks.map { it + SPAWN_OFFSET },
                center = localCenter + SPAWN_OFFSET,
                color = I_COLOR
            )
        }

        fun createOPiece(): Piece {
            // Local: 2x2 O-piece. Pivot is top-left X.
            // X X
            // X X
            val localBlocks = listOf(Position(0,0), Position(0,1), Position(1,0), Position(1,1))
            val localCenter = Position(0,0) // Rotation doesn't change O, but center is needed.
            return Piece(
                type = PieceType.O,
                blocks = localBlocks.map { it + SPAWN_OFFSET },
                center = localCenter + SPAWN_OFFSET,
                color = O_COLOR
            )
        }

        fun createTPiece(): Piece {
            // Local: T-piece. Pivot is the center X of the horizontal bar.
            // . X .
            // X X X
            val localBlocks = listOf(Position(0,1), Position(1,0), Position(1,1), Position(1,2))
            val localCenter = Position(1,1)
            return Piece(
                type = PieceType.T,
                blocks = localBlocks.map { it + SPAWN_OFFSET },
                center = localCenter + SPAWN_OFFSET,
                color = T_COLOR
            )
        }

        fun createSPiece(): Piece {
            // Local: S-piece. Pivot is the bottom-left X of the top bar.
            // . X X
            // X X .
            val localBlocks = listOf(Position(0,1), Position(0,2), Position(1,0), Position(1,1))
            val localCenter = Position(1,1) // Or (1,0) depending on rotation preference
            return Piece(
                type = PieceType.S,
                blocks = localBlocks.map { it + SPAWN_OFFSET },
                center = localCenter + SPAWN_OFFSET,
                color = S_COLOR
            )
        }

        fun createZPiece(): Piece {
            // Local: Z-piece. Pivot is the bottom-right X of the top bar.
            // X X .
            // . X X
            val localBlocks = listOf(Position(0,0), Position(0,1), Position(1,1), Position(1,2))
            val localCenter = Position(1,1) // Or (1,0)
            return Piece(
                type = PieceType.Z,
                blocks = localBlocks.map { it + SPAWN_OFFSET },
                center = localCenter + SPAWN_OFFSET,
                color = Z_COLOR
            )
        }

        fun createJPiece(): Piece {
            // Local: J-piece. Pivot is the middle X of the long bar.
            // X . .
            // X X X
            val localBlocks = listOf(Position(0,0), Position(1,0), Position(1,1), Position(1,2))
            val localCenter = Position(1,1)
            return Piece(
                type = PieceType.J,
                blocks = localBlocks.map { it + SPAWN_OFFSET },
                center = localCenter + SPAWN_OFFSET,
                color = J_COLOR
            )
        }

        fun createLPiece(): Piece {
            // Local: L-piece. Pivot is the middle X of the long bar.
            // . . X
            // X X X
            val localBlocks = listOf(Position(0,2), Position(1,0), Position(1,1), Position(1,2))
            val localCenter = Position(1,1)
            return Piece(
                type = PieceType.L,
                blocks = localBlocks.map { it + SPAWN_OFFSET },
                center = localCenter + SPAWN_OFFSET,
                color = L_COLOR
            )
        }

        /**
         * Returns a list of all piece factory functions.
         */
        fun getAllPieceFactories(): List<() -> Piece> {
            return listOf(
                ::createIPiece, ::createOPiece, ::createTPiece,
                ::createSPiece, ::createZPiece, ::createJPiece, ::createLPiece
            )
        }
    }
}
