package com.quantumblocks.game.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Sealed hierarchy representing a tetromino piece with its blocks, rotation center, and color.
 * Each concrete subclass corresponds to a specific piece shape.
 */
@Immutable
sealed class Piece {
    abstract val blocks: List<Position>

    /** The absolute board coordinates of the piece's rotation center. */
    abstract val center: Position
    abstract val color: Color

    /**
     * Rotates the piece 90 degrees clockwise around its center.
     * Default implementation applies to all but O piece which overrides to no-op.
     */
    //TODO: Evaluate options for this open function
    open fun rotate(): Piece {
        val rotatedBlocks = blocks.map { blockPosition ->
            val relativePos = blockPosition - center
            val rotatedRelativePos = Position(row = -relativePos.col, col = relativePos.row)
            center + rotatedRelativePos
        }
        return copyWith(blocks = rotatedBlocks)
    }

    /** Move by direction (delta row/col). */
    open fun move(direction: Position): Piece =
        copyWith(blocks = blocks.map { it + direction }, center = center + direction)

    /** Subclasses implement to create a modified copy preserving concrete type. */
    protected abstract fun copyWith(
        blocks: List<Position> = this.blocks,
        center: Position = this.center
    ): Piece

    // Concrete piece types ----------------------------------------------------
    @Immutable
    data class IPiece(override val blocks: List<Position>, override val center: Position) :
        Piece() {
        override val color: Color = Color(0xFF00FFFF) // Cyan
        override fun copyWith(blocks: List<Position>, center: Position): Piece =
            copy(blocks = blocks, center = center)
    }

    @Immutable
    data class OPiece(override val blocks: List<Position>, override val center: Position) :
        Piece() {
        override val color: Color = Color(0xFFFFFF00) // Yellow
        override fun rotate(): Piece = this // O piece doesn't rotate
        override fun copyWith(blocks: List<Position>, center: Position): Piece =
            copy(blocks = blocks, center = center)
    }

    @Immutable
    data class TPiece(override val blocks: List<Position>, override val center: Position) :
        Piece() {
        override val color: Color = Color(0xFF800080) // Purple
        override fun copyWith(blocks: List<Position>, center: Position): Piece =
            copy(blocks = blocks, center = center)
    }

    @Immutable
    data class SPiece(override val blocks: List<Position>, override val center: Position) :
        Piece() {
        override val color: Color = Color(0xFF00FF00) // Green
        override fun copyWith(blocks: List<Position>, center: Position): Piece =
            copy(blocks = blocks, center = center)
    }

    @Immutable
    data class ZPiece(override val blocks: List<Position>, override val center: Position) :
        Piece() {
        override val color: Color = Color(0xFFFF0000) // Red
        override fun copyWith(blocks: List<Position>, center: Position): Piece =
            copy(blocks = blocks, center = center)
    }

    @Immutable
    data class JPiece(override val blocks: List<Position>, override val center: Position) :
        Piece() {
        override val color: Color = Color(0xFF0000FF) // Blue
        override fun copyWith(blocks: List<Position>, center: Position): Piece =
            copy(blocks = blocks, center = center)
    }

    @Immutable
    data class LPiece(override val blocks: List<Position>, override val center: Position) :
        Piece() {
        override val color: Color = Color(0xFFFFA500) // Orange
        override fun copyWith(blocks: List<Position>, center: Position): Piece =
            copy(blocks = blocks, center = center)
    }

    companion object {
        // Initial spawn offset for pieces (near top-center of a 10-wide board)
        private val SPAWN_OFFSET = Position(0, 4)

        // Factory methods returning concrete piece instances ------------------
        fun createIPiece(): Piece = IPiece(
            blocks = listOf(
                Position(1, 0),
                Position(1, 1),
                Position(1, 2),
                Position(1, 3)
            ).map { it + SPAWN_OFFSET },
            center = Position(1, 1) + SPAWN_OFFSET
        )

        fun createOPiece(): Piece = OPiece(
            blocks = listOf(
                Position(0, 0),
                Position(0, 1),
                Position(1, 0),
                Position(1, 1)
            ).map { it + SPAWN_OFFSET },
            center = Position(0, 0) + SPAWN_OFFSET
        )

        fun createTPiece(): Piece = TPiece(
            blocks = listOf(
                Position(0, 1),
                Position(1, 0),
                Position(1, 1),
                Position(1, 2)
            ).map { it + SPAWN_OFFSET },
            center = Position(1, 1) + SPAWN_OFFSET
        )

        fun createSPiece(): Piece = SPiece(
            blocks = listOf(
                Position(0, 1),
                Position(0, 2),
                Position(1, 0),
                Position(1, 1)
            ).map { it + SPAWN_OFFSET },
            center = Position(1, 1) + SPAWN_OFFSET
        )

        fun createZPiece(): Piece = ZPiece(
            blocks = listOf(
                Position(0, 0),
                Position(0, 1),
                Position(1, 1),
                Position(1, 2)
            ).map { it + SPAWN_OFFSET },
            center = Position(1, 1) + SPAWN_OFFSET
        )

        fun createJPiece(): Piece = JPiece(
            blocks = listOf(
                Position(0, 0),
                Position(1, 0),
                Position(1, 1),
                Position(1, 2)
            ).map { it + SPAWN_OFFSET },
            center = Position(1, 1) + SPAWN_OFFSET
        )

        fun createLPiece(): Piece = LPiece(
            blocks = listOf(
                Position(0, 2),
                Position(1, 0),
                Position(1, 1),
                Position(1, 2)
            ).map { it + SPAWN_OFFSET },
            center = Position(1, 1) + SPAWN_OFFSET
        )

        /** Returns a list of all piece factory functions. */
        fun getAllPieceFactories(): List<() -> Piece> = listOf(
            ::createIPiece, ::createOPiece, ::createTPiece,
            ::createSPiece, ::createZPiece, ::createJPiece, ::createLPiece
        )
    }
}
