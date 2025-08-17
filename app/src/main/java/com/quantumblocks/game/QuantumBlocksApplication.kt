package com.quantumblocks.game

import android.app.Application
import com.quantumblocks.game.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class QuantumBlocksApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidLogger()
            androidContext(this@QuantumBlocksApplication)
            modules(appModule)
        }
    }
}
