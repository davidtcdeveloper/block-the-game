package com.quantumblocks.game.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Wrapper class that holds the game state as a StateFlow.
 * Acts as the single source of truth for state updates.
 */
class GameStateHolder(initialState: GameState = GameState()) {
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<GameState> = _state.asStateFlow()

    /**
     * Updates the game state. All state updates should go through this method.
     */
    fun updateState(newState: GameState) {
        _state.value = newState
    }

    /**
     * Gets the current state value.
     */
    fun getCurrentState(): GameState = _state.value
}

