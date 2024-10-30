package com.practical.data.repository

import com.apollographql.apollo.ApolloClient
import com.data.graphql.CharactersQuery
import com.practical.domain.CharacterModel
import com.practical.domain.repository.CharacterRepository
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : CharacterRepository {

    override suspend fun getCharacters(): List<CharacterModel> {
        val response = apolloClient.query(CharactersQuery()).execute()

        // Handle the response and map it to domain models
        return response.data?.characters?.results?.map { character ->
            CharacterModel(
                name = character?.name.orEmpty(),
                species = character?.species.orEmpty(),
                gender = character?.gender.orEmpty(),
                status = character?.status.orEmpty(),
                image = character?.image.orEmpty(),
                origin = character?.origin?.name.orEmpty(),
                location = character?.location?.dimension.orEmpty()
            )
        } ?: emptyList()
    }

}