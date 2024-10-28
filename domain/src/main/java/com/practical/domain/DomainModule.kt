package com.practical.domain

import com.practical.domain.repository.CharacterRepository
import com.practical.domain.usecases.GetCharactersUseCase
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    @Provides
    fun provideMyUseCase(repository: CharacterRepository): GetCharactersUseCase {
        return GetCharactersUseCase(repository)
    }
}