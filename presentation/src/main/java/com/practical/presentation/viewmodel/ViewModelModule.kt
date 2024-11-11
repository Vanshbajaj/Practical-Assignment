package com.practical.presentation.viewmodel

import com.practical.domain.usecases.GetCharacterUseCase
import com.practical.domain.usecases.GetCharactersUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ViewModelModule {
    @Provides
    @Singleton
    fun provideViewModelFactory(
        getCharactersUseCase: GetCharactersUseCase,
        getCharacterUseCase: GetCharacterUseCase,

    ): ViewModelFactory {
        return ViewModelFactory(getCharactersUseCase, getCharacterUseCase)
    }
}
