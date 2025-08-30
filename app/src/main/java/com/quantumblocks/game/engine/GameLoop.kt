package com.quantumblocks.game.engine

import com.quantumblocks.game.model.GameState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * Game loop that handles the automatic falling of pieces and triggers new piece spawning.
 */
class GameLoop(
    private val gameEngine: GameEngine,
    private val gameStateFlow: MutableStateFlow<GameState>,
    // TODO: move away from this callback
    private val spawnNewPieceCallback: () -> Unit, // Callback to request a new piece
    coroutineScope: CoroutineScope,
    dispatcher: CoroutineDispatcher,
) {
    private var gameJob: Job =
        coroutineScope.launch(dispatcher) {
            while (!gameStateFlow.value.gameOver) {
                val fallDelay = gameEngine.getFallDelay(gameStateFlow.value.level)
                delay(fallDelay)

                if (!gameStateFlow.value.gameOver) {
                    // Move piece down and update state
                    val nextState = gameEngine.movePieceDown(gameStateFlow.value)
                    gameStateFlow.value = nextState

                    // If the piece locked and a new one is needed (and game is not over from locking)
                    if (nextState.needsNewPiece && !nextState.gameOver) {
                        spawnNewPieceCallback() // ViewModel will update gameStateFlow via GameEngine
                    }
                }
            }
        }

    /**
     * Stops the game loop
     */
    fun stopGameLoop() {
        gameJob.cancel()
    }

    /**
     * Checks if the game loop is currently running
     */
    fun isRunning(): Boolean = gameJob.isActive
}
