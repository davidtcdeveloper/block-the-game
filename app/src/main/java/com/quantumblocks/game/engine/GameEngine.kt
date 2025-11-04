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
 * The engine is stateless - all state is stored in GameState.
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
            is GameCommand.SpawnPieceCommand -> handleSpawnPiece(currentState)
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
     * Reads the nextPiece from state and spawns it.
     * If nextPiece is null, generates one from the collection in state.
     */
    private fun handleSpawnPiece(gameState: GameState): GameState {
        val pieceToSpawn: Piece
        val stateAfterGettingPiece: GameState

        if (gameState.nextPiece != null) {
            // Use the nextPiece from state
            pieceToSpawn = gameState.nextPiece
            stateAfterGettingPiece = gameState.copy(nextPiece = null)
        } else {
            // Generate from collection (first spawn case)
            val (piece, updatedState) = gameState.getNextPieceFromCollection()
            pieceToSpawn = piece
            stateAfterGettingPiece = updatedState
        }

        // Spawn the piece
        val stateAfterSpawn = spawnSpecificPiece(stateAfterGettingPiece, pieceToSpawn)

        // Generate the next nextPiece for future spawn
        val (nextNextPiece, stateWithNextPiece) = stateAfterSpawn.getNextPieceFromCollection()

        // Return state with spawned piece and new nextPiece
        return stateWithNextPiece.copy(nextPiece = nextNextPiece)
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
            val isSingleBlockPiece = currentPiece is Piece.SingleBlockPiece
            val newStateWithPlacedPiece = gameState.placePiece(currentPiece)

            // If it's a SingleBlockPiece, fill empty rows below before clearing lines
            val stateAfterFilling = if (isSingleBlockPiece) {
                fillEmptyRowsBelow(
                    gameState = newStateWithPlacedPiece,
                    placedRow = currentPiece.center.row,
                    placedColumn = currentPiece.center.col
                )
            } else {
                newStateWithPlacedPiece
            }

            // Clear any completed lines (this will also calculate score)
            val stateAfterClearLines = stateAfterFilling.clearLines()

            // Generate the next piece from collection and store it in state
            val (nextPiece, stateWithNextPiece) = stateAfterClearLines.getNextPieceFromCollection()
            stateWithNextPiece.copy(nextPiece = nextPiece)
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
     * Fills all completely empty rows below the specified row with single blocks (one per column).
     * Only fills rows that are completely empty (all cells are false).
     */
    private fun fillEmptyRowsBelow(gameState: GameState, placedRow: Int, placedColumn: Int): GameState {
        val newBoard = gameState.board.map { it.toMutableList() }.toMutableList()
        // Start from the row below the placed piece
        for (row in (placedRow + 1) until gameState.boardHeight) {
            // Fill the entire row with single blocks (set all cells to true)
            newBoard[row][placedColumn] = true
        }
        return gameState.copy(board = newBoard.map { it.toList() })
    }

    /**
     * Calculates the fall delay based on the current score.
     * Speed increases by 10% every time the score reaches a multiple of 1000.
     */
    fun getFallDelay(score: Int): Long {
        val milestones = score / 1000 // Number of 1000-point milestones reached
        return (INITIAL_FALL_DELAY_MS * (LEVEL_SPEED_MULTIPLIER.pow(milestones))).toLong()
    }

    /**
     * Resets the game to an initial empty state.
     * Fills the piece collection and generates the first nextPiece so it's ready for the first spawn.
     */
    private fun resetGame(): GameState {
        // Create initial state with filled piece collection
        val initialState = GameState().withFilledPieceCollection()
        // Generate the first nextPiece for the new game
        val (firstNextPiece, stateWithCollection) = initialState.getNextPieceFromCollection()
        // Returns a GameState with an empty board, score 0, level 1, no current piece, and the first nextPiece.
        return stateWithCollection.copy(nextPiece = firstNextPiece)
    }
}
