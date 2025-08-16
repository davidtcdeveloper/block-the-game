package com.quantumblocks.game.model

import androidx.compose.runtime.Immutable

/**
 * Represents a single block in the game
 */
@Immutable
data class Block(val position: Position, val isActive: Boolean = false)
