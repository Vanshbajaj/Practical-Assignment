package com.practical.assignment

import android.app.Application

class RickAndMorty : Application() {
    override fun onCreate() {
        super.onCreate()

        val appComponent by lazy {
             DaggerAppComponent.builder().build()
        }
    }


}