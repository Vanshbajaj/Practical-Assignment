package com.practical.data.network

import com.apollographql.apollo.ApolloClient
import com.practical.data.repository.CharacterRepositoryImpl
import com.practical.domain.repository.CharacterRepository
import com.practical.domain.usecases.GetCharacterByIdUseCase
import com.practical.domain.usecases.GetCharactersUseCase
import com.practical.domain.usecases.GetEpisodeUseCases
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DataModule {
    @Provides
    @Singleton
    fun provideCharacterRepository(apolloClient: ApolloClient): CharacterRepository {
        return CharacterRepositoryImpl(apolloClient)
    }

    @Provides
    @Singleton
    fun provideMyUseCase(repository: CharacterRepository): GetCharactersUseCase {
        return GetCharactersUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUseCase(repository: CharacterRepository): GetCharacterByIdUseCase {
        return GetCharacterByIdUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideEpisodeUseCase(repository: CharacterRepository): GetEpisodeUseCases {
        return GetEpisodeUseCases(repository)
    }

}
