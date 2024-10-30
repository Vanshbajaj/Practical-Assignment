package com.practical.assignment

import android.app.Application

class RickAndMorty : Application() {
    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()

        // Initialize Dagger component
        appComponent = DaggerAppComponent.builder()
            .build()
        appComponent.inject(this)
    }
}
