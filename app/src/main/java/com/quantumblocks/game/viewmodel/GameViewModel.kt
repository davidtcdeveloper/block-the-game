package com.quantumblocks.game.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quantumblocks.game.engine.GameEngine
import com.quantumblocks.game.model.GameState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val SOFT_DROP_DELAY_MS = 50L

/**
 * ViewModel that manages the game state and user interactions
 */
class GameViewModel : ViewModel() {
    
    private val gameEngine = GameEngine()
    private var gameJob: Job? = null
    private var softDropJob: Job? = null
    
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()
    
    init {
        startNewGame()
    }
    
    /**
     * Starts a new game
     */
    fun startNewGame() {
        gameJob?.cancel()
        softDropJob?.cancel()
        
        val initialState = gameEngine.resetGame()
        _gameState.value = gameEngine.spawnPiece(initialState)
        
        startGameLoop()
    }
    
    /**
     * Starts the main game loop
     */
    private fun startGameLoop() {
        gameJob = viewModelScope.launch {
            while (!_gameState.value.gameOver) {
                val fallDelay = gameEngine.getFallDelay(_gameState.value.level)
                delay(fallDelay)
                
                if (!_gameState.value.gameOver) {
                    _gameState.value = gameEngine.movePieceDown(_gameState.value)
                }
            }
        }
    }
    
    /**
     * Handles move left button press
     */
    fun onMoveLeft() {
        if (_gameState.value.gameOver) return
        _gameState.value = gameEngine.movePieceLeft(_gameState.value)
    }
    
    /**
     * Handles move right button press
     */
    fun onMoveRight() {
        if (_gameState.value.gameOver) return
        _gameState.value = gameEngine.movePieceRight(_gameState.value)
    }
    
    /**
     * Handles rotate button press
     */
    fun onRotate() {
        if (_gameState.value.gameOver) return
        _gameState.value = gameEngine.rotatePiece(_gameState.value)
    }
    
    /**
     * Handles drop button press (soft drop)
     */
    fun onDrop() {
        if (_gameState.value.gameOver) return
        _gameState.value = gameEngine.movePieceDown(_gameState.value)
    }
    
    /**
     * Starts soft drop (rapid falling)
     */
    fun onDropStart() {
        if (_gameState.value.gameOver) return
        softDropJob = viewModelScope.launch {
            while (true) {
                delay(SOFT_DROP_DELAY_MS)
                _gameState.value = gameEngine.movePieceDown(_gameState.value)
            }
        }
    }
    
    /**
     * Stops soft drop
     */
    fun onDropEnd() {
        softDropJob?.cancel()
        softDropJob = null
    }
    
    override fun onCleared() {
        super.onCleared()
        gameJob?.cancel()
        softDropJob?.cancel()
    }
}
