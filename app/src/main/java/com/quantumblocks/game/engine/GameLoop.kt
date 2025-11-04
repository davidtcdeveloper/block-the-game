package com.quantumblocks.game.engine

import com.quantumblocks.game.model.GameStateHolder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Game loop that emits time-based tick commands to the game engine.
 * Runs on a background dispatcher and automatically moves pieces down at level-based intervals.
 */
class GameLoop(
    private val gameEngine: GameEngine,
    private val gameStateHolder: GameStateHolder,
    coroutineScope: CoroutineScope,
    dispatcher: CoroutineDispatcher,
) {
    private var gameJob: Job =
        coroutineScope.launch(dispatcher) {
            while (!gameStateHolder.getCurrentState().gameOver) {
                val currentState = gameStateHolder.getCurrentState()
                val fallDelay = gameEngine.getFallDelay(currentState.score)
                delay(fallDelay)

                val stateAfterDelay = gameStateHolder.getCurrentState()
                if (!stateAfterDelay.gameOver) {
                    // Emit TickCommand to engine and update state
                    val nextState = gameEngine.processCommand(GameCommand.TickCommand, stateAfterDelay)
                    gameStateHolder.updateState(nextState)

                    // If the piece locked and a new one is needed (and game is not over from locking)
                    // The ViewModel will observe needsNewPiece and spawn the next piece
                    if (nextState.needsNewPiece && !nextState.gameOver) {
                        // Signal is handled by ViewModel observing the state
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
