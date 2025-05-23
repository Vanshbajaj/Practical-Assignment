package com.practical.presentation.di

import com.practical.domain.usecases.GetCharacterByIdUseCase
import com.practical.domain.usecases.GetCharactersUseCase
import com.practical.domain.usecases.GetEpisodeUseCases
import com.practical.presentation.factory.ViewModelFactory
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
class ViewModelModule {
    @Provides
    @Singleton
    fun provideViewModelFactory(
        getCharactersUseCase: GetCharactersUseCase,
        getCharacterByIdUseCase: GetCharacterByIdUseCase,
        getEpisodeUseCases: GetEpisodeUseCases,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    ): ViewModelFactory {
        return ViewModelFactory(
            getCharactersUseCase,
            getCharacterByIdUseCase,
            getEpisodeUseCases,
            coroutineDispatcher
        )
    }
}
