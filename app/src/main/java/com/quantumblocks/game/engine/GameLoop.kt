package com.quantumblocks.game.engine

import com.quantumblocks.game.model.GameState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Game loop that handles the automatic falling of pieces
 */
class GameLoop(
    private val gameEngine: GameEngine,
    private val coroutineScope: CoroutineScope,
    private val gameStateFlow: MutableStateFlow<GameState>
) {

    // Launches the game job when the class is initialized
    private var gameJob: Job = coroutineScope.launch {
        while (!gameStateFlow.value.gameOver) {
            val fallDelay = gameEngine.getFallDelay(gameStateFlow.value.level)
            delay(fallDelay)

            if (!gameStateFlow.value.gameOver) {
                gameStateFlow.value = gameEngine.movePieceDown(gameStateFlow.value)
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
    fun isRunning(): Boolean {
        return gameJob.isActive
    }
}
