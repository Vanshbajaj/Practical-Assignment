package com.practical.data.repository

import com.apollographql.apollo3.ApolloClient
import com.domain.graphql.CharactersQuery
import com.practical.domain.CharacterDataClass
import com.practical.domain.repository.CharacterRepository
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : CharacterRepository {

    override suspend fun getCharacters(): List<CharacterDataClass> {
        val response = apolloClient.query(CharactersQuery()).execute()

        // Handle the response and map it to domain models
        return response.data?.characters?.results?.map {
            CharacterDataClass(
                name = it?.name ?: "",
                species = it?.species ?: "",
                gender = it?.gender ?: "",
                status = it?.status ?: "",
                image = it?.image ?: "",
                origin = it?.origin?.name ?: "",
                location = it?.location?.dimension ?: ""
            )
        } ?: emptyList()
    }
}
