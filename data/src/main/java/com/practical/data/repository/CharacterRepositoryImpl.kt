package com.practical.data.repository

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.data.graphql.CharactersQuery
import com.practical.domain.CharacterModel
import com.practical.domain.ResultState
import com.practical.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient,
) : CharacterRepository {

        override fun getCharacters(): Flow<ResultState<List<CharacterModel>>> = flow {
            // Emit loading state before fetching data
            emit(ResultState.Loading)

            // Fetch the characters from Apollo Client
            val response = apolloClient.query(CharactersQuery()).execute()

            // Check for successful data
            if (response.hasErrors()) {
                // If there are errors in the response, emit an error state
                emit(ResultState.Error(Exception("GraphQL errors: ${response.errors}")))
                return@flow // Exit the flow
            }

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

            // Emit success state with characters
            emit(ResultState.Success(characters))
        }.catch { e ->
            // Log the error
//            Log.e("CharacterRepository", "Error fetching characters", e)
            // Emit error state
            emit(ResultState.Error(e))
        }
    }
