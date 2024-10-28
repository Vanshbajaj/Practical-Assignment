package com.practical.data.network

import com.apollographql.apollo.ApolloClient
import com.practical.data.repository.CharacterRepositoryImpl
import com.practical.domain.repository.CharacterRepository
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
}