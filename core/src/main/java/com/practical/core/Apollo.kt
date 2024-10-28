package com.practical.core

import com.apollographql.apollo.ApolloClient
import com.practical.common.Constants
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
@Module
object Apollo {
    @Provides
    @Singleton
    fun provideApolloClient(): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(Constants.RICK_MORTY_URL)
            .build()
    }

}