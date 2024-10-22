package com.practical.data.network

import com.apollographql.apollo3.ApolloClient
import com.practical.data.repository.CharacterRepositoryImpl
import com.practical.domain.repository.CharacterRepository
import com.practical.domain.usecases.GetCharactersUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DataModule {
    @Provides
    @Singleton
    fun provideApolloClient(): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl("https://rickandmortyapi.com/graphql")
            .build()
    }

    @Provides
    @Singleton
    fun provideCharacterRepository(apolloClient: ApolloClient
    ): CharacterRepository = CharacterRepositoryImpl(apolloClient)

    @Provides
    @Singleton
    fun provideGetCharactersUseCase(
        repository: CharacterRepository
    ): GetCharactersUseCase = GetCharactersUseCase(repository)
}
