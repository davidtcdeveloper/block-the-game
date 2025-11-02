package com.quantumblocks.game.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quantumblocks.game.engine.GameCommand
import com.quantumblocks.game.engine.GameEngine
import com.quantumblocks.game.engine.GameLoop
import com.quantumblocks.game.model.GameState
import com.quantumblocks.game.model.GameStateHolder
import com.quantumblocks.game.model.Piece
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

private const val SOFT_DROP_DELAY_MS = 50L

/**
 * ViewModel that translates UI events to game commands and manages piece collection.
 * Observes game state to spawn new pieces when needed.
 */
class GameViewModel(
    private val gameEngine: GameEngine,
    private val gameLoopFactory: () -> GameLoop,
    private val gameStateHolder: GameStateHolder,
) : ViewModel() {
    private var softDropJob: Job? = null
    private var gameLoop: GameLoop? = null

    val gameState: StateFlow<GameState> = gameStateHolder.state

    private val pieceFactories = Piece.getAllPieceFactories()
    private var pieceCollection: MutableList<() -> Piece> = mutableListOf()

    init {
        // Observe state to spawn new pieces when needed
        viewModelScope.launch {
            gameState.collect { state ->
                if (state.needsNewPiece && !state.gameOver) {
                    spawnNewPiece()
                }
            }
        }
        startNewGame()
    }

    // Renamed from bag into collection
    private fun fillPieceCollection() {
        pieceCollection = pieceFactories.shuffled().toMutableList()
    }

    private fun getNextPieceFromCollection(): Piece {
        if (pieceCollection.isEmpty()) {
            fillPieceCollection()
        }
        return pieceCollection.removeFirst().invoke()
    }

    /**
     * Sends a command to the engine and updates the state holder.
     */
    private fun sendCommand(command: GameCommand) {
        val currentState = gameStateHolder.getCurrentState()
        val newState = gameEngine.processCommand(command, currentState)
        gameStateHolder.updateState(newState)
    }

    /**
     * Spawns a new piece from the collection onto the game board.
     * Called when a piece locks and needsNewPiece is true.
     */
    private fun spawnNewPiece() {
        val currentState = gameStateHolder.getCurrentState()
        if (currentState.gameOver) return

        val nextPiece = getNextPieceFromCollection()
        sendCommand(GameCommand.SpawnPieceCommand(nextPiece))

        // If the new piece immediately caused a game over, stop game loop and soft drop
        val stateAfterSpawn = gameStateHolder.getCurrentState()
        if (stateAfterSpawn.gameOver) {
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
        fillPieceCollection()

        // Reset game state
        sendCommand(GameCommand.ResetGameCommand)

        // Spawn first piece
        val firstPiece = getNextPieceFromCollection()
        sendCommand(GameCommand.SpawnPieceCommand(firstPiece))

        // Check if the game is over after attempting to spawn the first piece
        val stateAfterSpawn = gameStateHolder.getCurrentState()
        if (!stateAfterSpawn.gameOver) {
            // Create and start new game loop (starts automatically in constructor)
            gameLoop = gameLoopFactory()
        }
    }

    /**
     * Handles move left button press
     */
    fun onMoveLeft() {
        val currentState = gameStateHolder.getCurrentState()
        if (currentState.gameOver) return
        sendCommand(GameCommand.MoveLeftCommand)
    }

    /**
     * Handles move right button press
     */
    fun onMoveRight() {
        val currentState = gameStateHolder.getCurrentState()
        if (currentState.gameOver) return
        sendCommand(GameCommand.MoveRightCommand)
    }

    /**
     * Handles rotate button press
     */
    fun onRotate() {
        val currentState = gameStateHolder.getCurrentState()
        if (currentState.gameOver) return
        sendCommand(GameCommand.RotateCommand)
    }

    /**
     * Handles drop button press (moves piece down once)
     */
    fun onDrop() {
        val currentState = gameStateHolder.getCurrentState()
        if (currentState.gameOver) return
        sendCommand(GameCommand.MoveDownCommand)
    }

    /**
     * Starts soft drop (rapid falling)
     */
    fun onDropStart() {
        val currentState = gameStateHolder.getCurrentState()
        if (currentState.gameOver) {
            return
        }
        softDropJob?.cancel()
        sendCommand(GameCommand.SoftDropStartCommand)
        softDropJob =
            viewModelScope.launch {
                while (true) {
                    delay(SOFT_DROP_DELAY_MS)
                    val state = gameStateHolder.getCurrentState()
                    if (state.gameOver || !state.softDropActive) {
                        break
                    }
                    sendCommand(GameCommand.MoveDownCommand)
                }
            }
    }

    /**
     * Stops soft drop
     */
    fun onDropEnd() {
        sendCommand(GameCommand.SoftDropStopCommand)
        softDropJob?.cancel()
        softDropJob = null
    }

    override fun onCleared() {
        super.onCleared()
        gameLoop?.stopGameLoop()
        softDropJob?.cancel()
    }
}
