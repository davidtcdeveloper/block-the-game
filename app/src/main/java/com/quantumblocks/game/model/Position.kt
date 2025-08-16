package com.quantumblocks.game.model

import androidx.compose.runtime.Immutable

/**
 * Represents a position on the game grid
 */
@Immutable
data class Position(val row: Int, val col: Int) {
    operator fun plus(other: Position): Position =
        Position(row + other.row, col + other.col)
    operator fun minus(other: Position): Position =
        Position(row - other.row, col - other.col)
}
