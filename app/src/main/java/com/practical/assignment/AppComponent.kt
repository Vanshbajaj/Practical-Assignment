package com.practical.assignment

import com.practical.data.network.DataModule
import com.practical.domain.DomainModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class, DomainModule::class])
interface AppComponent {
    fun inject(application: RickAndMorty)
    fun inject(activity: MainActivity)
    // Add other inject methods as needed
}
