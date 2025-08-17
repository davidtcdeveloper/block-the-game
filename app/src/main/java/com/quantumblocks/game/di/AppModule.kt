package com.quantumblocks.game.di

import com.quantumblocks.game.engine.GameEngine
import com.quantumblocks.game.engine.GameLoop
import com.quantumblocks.game.model.GameState
import com.quantumblocks.game.viewmodel.GameViewModel
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
    
    // Coroutine scope for game loop
    single<CoroutineScope> { 
        CoroutineScope(SupervisorJob() + Dispatchers.Main) 
    }
    
    // Factory function for creating GameLoop instances
    factory<(MutableStateFlow<GameState>) -> GameLoop> { 
        { gameStateFlow -> GameLoop(get(), get(), gameStateFlow) }
    }
    
    // ViewModel factory for GameViewModel
    viewModel<GameViewModel> { 
        GameViewModel(get(), get(), get()) 
    }
}
