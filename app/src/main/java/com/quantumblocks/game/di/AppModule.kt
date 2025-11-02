package com.quantumblocks.game.di

import com.quantumblocks.game.engine.GameEngine
import com.quantumblocks.game.engine.GameLoop
import com.quantumblocks.game.model.GameStateHolder
import com.quantumblocks.game.viewmodel.GameViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule =
    module {

        // Singleton instance of GameEngine
        single<GameEngine> {
            GameEngine()
        }

        // Singleton instance of GameStateHolder - single source of truth for game state
        single<GameStateHolder> {
            GameStateHolder()
        }

        // Coroutine scope for game loop
        single<CoroutineScope> {
            CoroutineScope(SupervisorJob() + Dispatchers.Default)
        }

        // Coroutine dispatcher for game loop
        single<CoroutineDispatcher> {
            Dispatchers.Default
        }

        // Factory function for creating GameLoop instances
        // Each game gets a new GameLoop instance that starts automatically
        factory<() -> GameLoop> {
            {
                GameLoop(
                    get(), // GameEngine
                    get(), // GameStateHolder
                    get(), // CoroutineScope for GameLoop
                    get(), // CoroutineDispatcher for GameLoop
                )
            }
        }

        // ViewModel factory for GameViewModel
        viewModel<GameViewModel> {
            GameViewModel(
                get(), // GameEngine
                get(), // GameLoop factory
                get(), // GameStateHolder
            )
        }
    }
