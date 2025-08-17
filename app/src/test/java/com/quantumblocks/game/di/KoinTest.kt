package com.quantumblocks.game.di

import com.quantumblocks.game.engine.GameEngine
import com.quantumblocks.game.viewmodel.GameViewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.get
import kotlin.test.Test
import kotlin.test.assertNotNull

/**
 * Simple test to verify Koin dependency injection is working
 */
class KoinTest : KoinTest {
    
    @Test
    fun testGameEngineInjection() {
        startKoin {
            modules(appModule)
        }
        
        val gameEngine: GameEngine = get()
        assertNotNull(gameEngine, "GameEngine should be injected")
        
        stopKoin()
    }
    
    @Test
    fun testGameViewModelInjection() {
        startKoin {
            modules(appModule)
        }
        
        val gameViewModel: GameViewModel = get()
        assertNotNull(gameViewModel, "GameViewModel should be injected")
        
        stopKoin()
    }
}
