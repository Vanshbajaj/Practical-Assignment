package com.practical.data.repository

import com.apollographql.apollo.ApolloClient
import com.data.graphql.CharacterDetailsQuery
import com.data.graphql.CharactersListQuery
import com.data.graphql.EpisodeQuery
import com.practical.common.Constants
import com.practical.data.toCharacterModel
import com.practical.data.toEpisodeModelDetails
import com.practical.domain.CharacterModel
import com.practical.domain.CharactersListModel
import com.practical.domain.EpisodeModelDetails
import com.practical.domain.ResultState
import com.practical.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient,
) : CharacterRepository {

    override fun getCharactersList(): Flow<ResultState<List<CharactersListModel>>> = flow {
        emit(ResultState.Loading)
        val response = apolloClient.query(CharactersListQuery()).execute()
        if (response.hasErrors()) {
            emit(ResultState.Error(Exception("GraphQL errors: ${response.errors}")))
            return@flow // Exit the flow
        }
        val characters = response.data?.characters?.results?.mapNotNull { character ->
            CharactersListModel(
                id = character?.id,
                name = character?.name,
                image = character?.image,
            )
        } ?: emptyList()
        emit(ResultState.Success(characters))
    }.catch { e ->
        emit(ResultState.Error(e))
    }

    override fun getCharacter(id: String): Flow<ResultState<CharacterModel>> {
        return flow {
            emit(ResultState.Loading)
            val response = apolloClient.query(CharacterDetailsQuery(id)).execute()
            if (response.hasErrors()) {
                emit(ResultState.Error(Exception(response.errors?.joinToString())))
            } else {
                response.data?.character?.toCharacterModel()?.let { character ->
                    emit(ResultState.Success(character))
                } ?: run {
                    emit(ResultState.Error(Exception(Constants.error)))
                }
            }
        }.catch { e ->
            emit(ResultState.Error(e))
        }
    }

    override fun getEpisodeId(id: String): Flow<ResultState<EpisodeModelDetails>> {
        return flow {
            emit(ResultState.Loading)
            val response = apolloClient.query(EpisodeQuery(id)).execute()
            if (response.hasErrors()) {
                emit(ResultState.Error(Exception(response.errors.toString())))
            } else {
                response.data?.episode?.toEpisodeModelDetails()?.let { character ->
                    emit(ResultState.Success(character))
                } ?: run {
                    emit(ResultState.Error(Exception(Constants.error)))
                }
            }
        }.catch { e ->
            emit(ResultState.Error(e))
        }
    }
}
