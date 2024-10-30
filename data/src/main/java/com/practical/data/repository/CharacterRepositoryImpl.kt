package com.practical.data.repository

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.exception.ApolloException
import com.data.graphql.CharactersQuery
import com.practical.domain.CharacterModel
import com.practical.domain.ResultState
import com.practical.domain.repository.CharacterRepository
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient,
) : CharacterRepository {
    override suspend fun getCharacters(): ResultState<List<CharacterModel>> {
        return try {
            // Indicate loading state
            ResultState.Loading

            val response = apolloClient.query(CharactersQuery()).execute()

            // Map results to CharacterModel
            val characters = response.data?.characters?.results?.map { character ->
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

            // Return success state with characters
            ResultState.Success(characters)
        } catch (e: ApolloException) {
            // Log the error
            Log.e("CharacterRepository", "Error fetching characters", e)
            // Return error state
            ResultState.Error(e)
        }
    }
}
