package com.quantumblocks.game.engine

import com.quantumblocks.game.model.GameState
import com.quantumblocks.game.model.Piece
import com.quantumblocks.game.model.Position
import kotlin.math.pow

private const val INITIAL_FALL_DELAY_MS = 1000L
private const val LEVEL_SPEED_MULTIPLIER = 0.9f

/**
 * Game engine that handles the core game logic
 */
class GameEngine {

    /**
     * Creates a new piece at the top center of the board
     */
    fun spawnPiece(gameState: GameState): GameState {
        val newPiece = Piece.createTPiece().move(Position(0, gameState.boardWidth / 2 - 1))
        
        return if (gameState.canPlacePiece(newPiece)) {
            gameState.copy(currentPiece = newPiece)
        } else {
            gameState.copy(gameOver = true)
        }
    }

    /**
     * Moves the current piece down by one position
     */
    fun movePieceDown(gameState: GameState): GameState {
        val currentPiece = gameState.currentPiece
        
        val movedPiece = currentPiece.move(Position(1, 0))
        
        return if (gameState.canPlacePiece(movedPiece)) {
            gameState.copy(currentPiece = movedPiece)
        } else {
            // Piece can't move down, lock it in place
            lockPiece(gameState)
        }
    }

    /**
     * Moves the current piece left by one position
     */
    fun movePieceLeft(gameState: GameState): GameState {
        val currentPiece = gameState.currentPiece
        
        val movedPiece = currentPiece.move(Position(0, -1))
        
        return if (gameState.canPlacePiece(movedPiece)) {
            gameState.copy(currentPiece = movedPiece)
        } else {
            gameState
        }
    }

    /**
     * Moves the current piece right by one position
     */
    fun movePieceRight(gameState: GameState): GameState {
        val currentPiece = gameState.currentPiece
        
        val movedPiece = currentPiece.move(Position(0, 1))
        
        return if (gameState.canPlacePiece(movedPiece)) {
            gameState.copy(currentPiece = movedPiece)
        } else {
            gameState
        }
    }

    /**
     * Rotates the current piece 90 degrees clockwise
     */
    fun rotatePiece(gameState: GameState): GameState {
        val currentPiece = gameState.currentPiece
        
        val rotatedPiece = currentPiece.rotate()
        
        return if (gameState.canPlacePiece(rotatedPiece)) {
            gameState.copy(currentPiece = rotatedPiece)
        } else {
            gameState
        }
    }

    /**
     * Locks the current piece in place and spawns a new one
     */
    private fun lockPiece(gameState: GameState): GameState {
        val currentPiece = gameState.currentPiece
        
        var newState = gameState.placePiece(currentPiece)
        newState = newState.clearLines()
        newState = spawnPiece(newState)
        
        return newState
    }

    /**
     * Calculates the fall delay based on the current level
     */
    fun getFallDelay(level: Int): Long {
        return (INITIAL_FALL_DELAY_MS * (LEVEL_SPEED_MULTIPLIER.pow(level - 1))).toLong()
    }

    /**
     * Resets the game to initial state
     */
    fun resetGame(): GameState {
        return GameState()
    }
}
