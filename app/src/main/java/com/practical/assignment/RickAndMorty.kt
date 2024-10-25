package com.practical.assignment

import android.app.Application

class RickAndMorty : Application() {
    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().build()
        val appComponent by lazy {
             DaggerAppComponent.builder().build()
        }
    }


}