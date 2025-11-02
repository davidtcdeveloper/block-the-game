package com.quantumblocks.game.engine

import com.quantumblocks.game.model.GameState
import com.quantumblocks.game.model.Piece
import com.quantumblocks.game.model.Position
import kotlin.math.pow

private const val INITIAL_FALL_DELAY_MS = 1000L
private const val LEVEL_SPEED_MULTIPLIER = 0.9f

/**
 * Game engine that processes commands and updates game state.
 * Implements a state machine where commands transition the game from one state to another.
 */
class GameEngine {
    /**
     * Processes a command and returns the new game state.
     * This is the single entry point for all state transitions.
     */
    fun processCommand(command: GameCommand, currentState: GameState): GameState {
        return when (command) {
            is GameCommand.TickCommand -> handleTick(currentState)
            is GameCommand.MoveLeftCommand -> handleMoveLeft(currentState)
            is GameCommand.MoveRightCommand -> handleMoveRight(currentState)
            is GameCommand.MoveDownCommand -> handleMoveDown(currentState)
            is GameCommand.RotateCommand -> handleRotate(currentState)
            is GameCommand.SpawnPieceCommand -> handleSpawnPiece(currentState, command.piece)
            is GameCommand.StartGameCommand -> currentState // StartGame handled by ViewModel logic
            is GameCommand.ResetGameCommand -> handleResetGame()
            is GameCommand.SoftDropStartCommand -> handleSoftDropStart(currentState)
            is GameCommand.SoftDropStopCommand -> handleSoftDropStop(currentState)
        }
    }

    /**
     * Handles tick command - moves piece down automatically.
     */
    private fun handleTick(gameState: GameState): GameState {
        return movePieceDown(gameState)
    }

    /**
     * Handles move left command.
     */
    private fun handleMoveLeft(gameState: GameState): GameState {
        return movePieceLeft(gameState)
    }

    /**
     * Handles move right command.
     */
    private fun handleMoveRight(gameState: GameState): GameState {
        return movePieceRight(gameState)
    }

    /**
     * Handles move down command.
     */
    private fun handleMoveDown(gameState: GameState): GameState {
        return movePieceDown(gameState)
    }

    /**
     * Handles rotate command.
     */
    private fun handleRotate(gameState: GameState): GameState {
        return rotatePiece(gameState)
    }

    /**
     * Handles spawn piece command.
     */
    private fun handleSpawnPiece(gameState: GameState, piece: Piece): GameState {
        return spawnSpecificPiece(gameState, piece)
    }

    /**
     * Handles reset game command.
     */
    private fun handleResetGame(): GameState {
        return resetGame()
    }

    /**
     * Handles soft drop start command.
     */
    private fun handleSoftDropStart(gameState: GameState): GameState {
        if (gameState.gameOver) return gameState
        return gameState.copy(softDropActive = true)
    }

    /**
     * Handles soft drop stop command.
     */
    private fun handleSoftDropStop(gameState: GameState): GameState {
        return gameState.copy(softDropActive = false)
    }

    /**
     * Attempts to spawn the given piece onto the board.
     * If the piece cannot be placed (e.g., no space), it sets gameOver to true.
     * This also resets the needsNewPiece flag from the GameState.
     */
    private fun spawnSpecificPiece(
        gameState: GameState,
        piece: Piece,
    ): GameState {
        // The piece comes with its initial position from the Piece factory
        return if (gameState.canPlacePiece(piece)) {
            gameState.copy(currentPiece = piece, gameOver = false, needsNewPiece = false)
        } else {
            // Cannot place the new piece, so it's game over.
            // Keep the board as is, but mark game over and ensure no floating piece.
            gameState.copy(currentPiece = null, gameOver = true, needsNewPiece = false)
        }
    }

    /**
     * Moves the current piece down by one position.
     * If the piece cannot move down, it locks the piece in place.
     */
    private fun movePieceDown(gameState: GameState): GameState {
        val currentPiece = gameState.currentPiece ?: return gameState // No piece to move
        if (gameState.gameOver) return gameState

        val movedPiece = currentPiece.move(Position(1, 0))

        return if (gameState.canPlacePiece(movedPiece)) {
            gameState.copy(currentPiece = movedPiece)
        } else {
            // Piece can't move down, lock it in place.
            // placePiece in GameState will set needsNewPiece = true
            val newStateWithPlacedPiece = gameState.placePiece(currentPiece)
            // Then clear any completed lines.
            newStateWithPlacedPiece.clearLines()
        }
    }

    /**
     * Moves the current piece left by one position if possible.
     */
    private fun movePieceLeft(gameState: GameState): GameState {
        val currentPiece = gameState.currentPiece ?: return gameState
        if (gameState.gameOver) {
            return gameState
        }

        val movedPiece = currentPiece.move(Position(0, -1))

        return if (gameState.canPlacePiece(movedPiece)) {
            gameState.copy(currentPiece = movedPiece)
        } else {
            gameState
        }
    }

    /**
     * Moves the current piece right by one position if possible.
     */
    private fun movePieceRight(gameState: GameState): GameState {
        val currentPiece = gameState.currentPiece ?: return gameState
        if (gameState.gameOver) {
            return gameState
        }

        val movedPiece = currentPiece.move(Position(0, 1))

        return if (gameState.canPlacePiece(movedPiece)) {
            gameState.copy(currentPiece = movedPiece)
        } else {
            gameState
        }
    }

    /**
     * Rotates the current piece 90 degrees clockwise if possible.
     */
    private fun rotatePiece(gameState: GameState): GameState {
        val currentPiece = gameState.currentPiece ?: return gameState
        if (gameState.gameOver) return gameState

        val rotatedPiece = currentPiece.rotate()

        // Check for wall kicks or other adjustments if needed in the future.
        // For now, simple rotation if valid.
        return if (gameState.canPlacePiece(rotatedPiece)) {
            gameState.copy(currentPiece = rotatedPiece)
        } else {
            // Advanced: Could try wall kicks here.
            // For now, if direct rotation is invalid, do nothing.
            gameState
        }
    }

    /**
     * Calculates the fall delay based on the current level.
     */
    fun getFallDelay(level: Int): Long = (INITIAL_FALL_DELAY_MS * (LEVEL_SPEED_MULTIPLIER.pow(level - 1))).toLong()

    /**
     * Resets the game to an initial empty state.
     * The first piece will be spawned by GameViewModel.
     */
    private fun resetGame(): GameState {
        // Returns a GameState with an empty board, score 0, level 1, no current piece, and not needing a new piece.
        return GameState()
    }
}
