package com.practical.data.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.exception.ApolloException
import com.data.graphql.CharactersListQuery
import com.data.graphql.EpisodeQuery
import com.practical.data.network.ApolloClientException
import com.practical.data.network.ClientNetworkException
import com.practical.data.network.NetworkException
import com.practical.data.toEpisodeModelDetails
import com.practical.domain.CharacterModel
import com.practical.domain.CharactersListModel
import com.practical.domain.EpisodeModelDetails
import com.practical.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient,
) : CharacterRepository {

    override fun getCharactersList(): Flow<List<CharactersListModel>> = flow {
        val response = apolloClient.query(CharactersListQuery()).execute()
        if (response.hasErrors()) {
//            emit(ResultState.Error(Exception("GraphQL errors: ${response.errors}")))
            return@flow // Exit the flow
        }
        val characters = response.data?.characters?.results?.mapNotNull { character ->
            CharactersListModel(
                id = character?.id,
                name = character?.name,
                image = character?.image,
            )
        } ?: emptyList()
//        emit(ResultState.Success(characters))
    }.catch { e ->
//        emit(ResultState.Error(e))
    }

    override fun getCharacter(id: String): Flow<CharacterModel> {
        return flow {

        }
    }

    override fun getEpisodeId(id: String): Flow<EpisodeModelDetails> {
        return flow {
            val response = apolloClient.query(EpisodeQuery(id)).execute()
            try {
                response.data?.episode?.toEpisodeModelDetails()?.let { emit(it) }
            } catch (t: Throwable) {
                when (t) {
                    is NetworkException -> {
                        throw ClientNetworkException("Client network error occurred", t)
                    }

                    is ApolloException -> {
                        throw ApolloClientException("Apollo error", t)

                    }
                }
            }
        }
    }
}
