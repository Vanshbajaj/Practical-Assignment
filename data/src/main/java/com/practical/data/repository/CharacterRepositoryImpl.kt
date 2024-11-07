package com.practical.data.repository

import com.apollographql.apollo.ApolloClient
import com.data.graphql.CharactersListQuery
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
        val response = apolloClient.query(CharactersListQuery()).execute()

        // Check for successful data
        if (response.hasErrors()) {
            // If there are errors in the response, emit an error state
            emit(ResultState.Error(Exception("GraphQL errors: ${response.errors}")))
            return@flow // Exit the flow
        }


        val characters = response.data?.characters?.results?.map { character ->
            CharacterModel(
                id = character?.id.orEmpty(),
                name = character?.name.orEmpty(),
                image = character?.image.orEmpty(),
            )
        } ?: emptyList()

        // Emit success state with characters
        emit(ResultState.Success(characters))
    }.catch { e ->
        emit(ResultState.Error(e))
    }
}
