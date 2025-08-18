package com.quantumblocks.game.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quantumblocks.game.engine.GameEngine
import com.quantumblocks.game.engine.GameLoop
import com.quantumblocks.game.model.GameState
import com.quantumblocks.game.model.Piece
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
class GameViewModel(
    private val gameEngine: GameEngine, //TODO: Review if we need both engine and loop here
    private val gameLoopFactory: (MutableStateFlow<GameState>, () -> Unit) -> GameLoop
) : ViewModel() {

    private var softDropJob: Job? = null
    private var gameLoop: GameLoop? = null

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val pieceFactories = Piece.getAllPieceFactories()
    private var pieceBag: MutableList<() -> Piece> = mutableListOf()

    init {
        startNewGame()
    }

    // TODO: Rename from bag into collection
    private fun fillPieceBag() {
        pieceBag = pieceFactories.shuffled().toMutableList()
    }

    private fun getNextPieceFromBag(): Piece {
        if (pieceBag.isEmpty()) {
            fillPieceBag()
        }
        return pieceBag.removeFirst().invoke()
    }

    /**
     * Spawns a new piece from the bag onto the game board.
     * This will be called by the GameLoop when a piece locks and a new one is needed.
     */
    fun spawnNewPiece() {
        if (_gameState.value.gameOver) return

        val nextPiece = getNextPieceFromBag()
        // spawnSpecificPiece is expected to return a state with gameOver=true if spawning fails
        _gameState.value = gameEngine.spawnSpecificPiece(_gameState.value, nextPiece)

        // If the new piece immediately caused a game over (handled by spawnSpecificPiece),
        // ensure game loop and soft drop are stopped.
        if (_gameState.value.gameOver) {
            gameLoop?.stopGameLoop()
            softDropJob?.cancel()
        }
    }

    /**
     * Starts a new game
     */
    fun startNewGame() {
        gameLoop?.stopGameLoop()
        softDropJob?.cancel()
        fillPieceBag()

        var initialState = gameEngine.resetGame()
        val firstPiece = getNextPieceFromBag()
        // spawnSpecificPiece updates initialState, potentially setting gameOver=true
        initialState = gameEngine.spawnSpecificPiece(initialState, firstPiece)

        _gameState.value = initialState

        // Check if the game is over after attempting to spawn the first piece
        if (_gameState.value.gameOver) {
            // Game over on start, do not start game loop
        } else {
            // Pass the spawnNewPiece function reference to the GameLoop
            gameLoop = gameLoopFactory(_gameState, ::spawnNewPiece)
            // GameLoop should be started here if not started by its factory/constructor
            // For now, assuming factory handles GameLoop start or it's started elsewhere if needed.
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
        if (_gameState.value.gameOver) {
            return
        }
        softDropJob?.cancel()
        softDropJob = viewModelScope.launch {
            while (true) {
                delay(SOFT_DROP_DELAY_MS)
                if (_gameState.value.gameOver) {
                    break
                }
                val newState = gameEngine.movePieceDown(_gameState.value)
                _gameState.value = newState
                // GameLoop will handle calling spawnNewPiece if needsNewPiece is set in newState
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
        gameLoop?.stopGameLoop()
        softDropJob?.cancel()
    }
}
