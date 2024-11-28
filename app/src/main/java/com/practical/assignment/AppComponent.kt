package com.practical.assignment


import com.practical.core.ApolloModule
import com.practical.data.network.DataModule
import com.practical.presentation.di.DispatcherModule
import com.practical.presentation.di.ViewModelModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApolloModule::class, DataModule::class, ViewModelModule::class, DispatcherModule::class])
interface AppComponent {
    fun inject(application: RickAndMorty)
    fun inject(activity: MainActivity)
    // Add other inject methods as needed
}
