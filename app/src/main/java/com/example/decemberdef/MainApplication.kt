package com.example.decemberdef

import android.app.Application
import com.example.decemberdef.data.AppContainer
import com.example.decemberdef.data.DefaultAppContainer
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        val appContext by lazy { applicationContext }
        container = DefaultAppContainer()
    }
}