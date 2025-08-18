package com.quantumblocks.game.di

import com.quantumblocks.game.engine.GameEngine
import com.quantumblocks.game.engine.GameLoop
import com.quantumblocks.game.model.GameState
import com.quantumblocks.game.viewmodel.GameViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    
    // Singleton instance of GameEngine
    single<GameEngine> { 
        GameEngine() 
    }
    
    // Coroutine scope for game loop and viewmodel
    single<CoroutineScope> { 
        CoroutineScope(SupervisorJob() + Dispatchers.Main) 
    }

    // Coroutine dispatcher for game loop
    single<CoroutineDispatcher> {
        Dispatchers.Default
    }
    
    // Factory function for creating GameLoop instances
    // This provides the factory function itself, which GameViewModel will inject.
    factory<(MutableStateFlow<GameState>, () -> Unit) -> GameLoop> {
        // This outer lambda is what Koin calls to get the factory function.
        // It returns a function that matches GameViewModel's dependency.
        { gameStateFlow, spawnNewPieceCallback -> // These are the parameters of the factory function
            GameLoop(
                get(), // GameEngine
                gameStateFlow, // MutableStateFlow<GameState>
                spawnNewPieceCallback, // The () -> Unit callback
                get(), // CoroutineScope for GameLoop
                get()  // CoroutineDispatcher for GameLoop
            )
        }
    }
    
    // ViewModel factory for GameViewModel
    viewModel<GameViewModel> { 
        GameViewModel(
            get(), // GameEngine
            get(), // CoroutineScope for GameViewModel
        ) 
    }
}
