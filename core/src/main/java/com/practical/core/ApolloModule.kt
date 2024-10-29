package com.practical.core

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.okHttpClient
import com.practical.common.Constants
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
object ApolloModule {
    @Provides
    @Singleton
    fun provideApolloClient(): ApolloClient {
        val httpClient = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()

        return ApolloClient.Builder().serverUrl(Constants.RICK_MORTY_URL)
            .okHttpClient(httpClient) // Attach the OkHttpClient to Apollo
            .build()
    }
}


