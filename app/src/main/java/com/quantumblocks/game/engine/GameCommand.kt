package com.quantumblocks.game.engine

import com.quantumblocks.game.model.Piece

/**
 * Sealed interface representing all game commands.
 * Commands are processed by GameEngine to update game state.
 */
sealed interface GameCommand {
    /** Time-based fall event from GameLoop */
    data object TickCommand : GameCommand

    /** Move current piece left */
    data object MoveLeftCommand : GameCommand

    /** Move current piece right */
    data object MoveRightCommand : GameCommand

    /** Move current piece down */
    data object MoveDownCommand : GameCommand

    /** Rotate current piece 90 degrees clockwise */
    data object RotateCommand : GameCommand

    /** Spawn a new piece onto the board */
    data class SpawnPieceCommand(val piece: Piece) : GameCommand

    /** Start a new game */
    data object StartGameCommand : GameCommand

    /** Reset game to initial state */
    data object ResetGameCommand : GameCommand

    /** Start soft drop (rapid falling) */
    data object SoftDropStartCommand : GameCommand

    /** Stop soft drop */
    data object SoftDropStopCommand : GameCommand
}

