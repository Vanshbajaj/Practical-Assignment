package com.practical.assignment


import com.practical.core.Apollo
import com.practical.data.network.DataModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [Apollo::class,DataModule::class])
interface AppComponent {
    fun inject(application: RickAndMorty)
    fun inject(activity: MainActivity)
    // Add other inject methods as needed
}
