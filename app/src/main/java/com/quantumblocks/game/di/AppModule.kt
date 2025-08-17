package com.quantumblocks.game.di

import com.quantumblocks.game.engine.GameEngine
import com.quantumblocks.game.viewmodel.GameViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    
    // Singleton instance of GameEngine
    single<GameEngine> { 
        GameEngine() 
    }
    
    // ViewModel factory for GameViewModel
    viewModel<GameViewModel> { 
        GameViewModel(get()) 
    }
}
