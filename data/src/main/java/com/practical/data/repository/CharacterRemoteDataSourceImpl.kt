package com.practical.data.repository

import com.apollographql.apollo.ApolloClient
import com.data.graphql.CharactersQuery
import com.practical.domain.CharacterDataClass
import com.practical.domain.repository.CharacterRepository
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : CharacterRepository {

    override suspend fun getCharacters(): List<CharacterDataClass> {
        val response = apolloClient.query(CharactersQuery()).execute()

        // Handle the response and map it to domain models
        return response.data?.characters?.results?.map { character ->
            CharacterDataClass(
                name = character?.name ?: "",
                species = character?.species ?: "",
                gender = character?.gender ?: "",
                status = character?.status ?: "",
                image = character?.image ?: "",
                origin = character?.origin?.name ?: "",
                location = character?.location?.dimension ?: ""
            )
        } ?: emptyList()
    }
}