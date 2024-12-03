package com.practical.data.repository

import com.apollographql.apollo.ApolloClient
import com.data.graphql.CharacterDetailsQuery
import com.data.graphql.CharactersListQuery
import com.data.graphql.EpisodeQuery
import com.practical.data.network.NetworkException
import com.practical.data.toCharacterModel
import com.practical.data.toEpisodeModelDetails
import com.practical.domain.CharacterModel
import com.practical.domain.CharactersListModel
import com.practical.domain.EpisodeModelDetails
import com.practical.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient,
) : CharacterRepository {

    override fun getCharactersList(): Flow<List<CharactersListModel>> {
        return flow {
            val response = apolloClient.query(CharactersListQuery()).execute()
            val characters = response.data?.characters?.results?.mapNotNull { character ->
                CharactersListModel(
                    id = character?.id,
                    name = character?.name,
                    image = character?.image,
                )
            } ?: throw NetworkException.ClientNetworkException
            emit(characters)
        }
    }

    override fun getCharacter(id: String): Flow<CharacterModel> {
        return flow {
            val response = apolloClient.query(CharacterDetailsQuery(id)).execute()
            response.data?.character?.toCharacterModel()?.let {
                emit(it) // Emit character data
            } ?: throw NetworkException.ClientNetworkException
        }
    }

    override fun getEpisodeId(id: String): Flow<EpisodeModelDetails> {
        return flow {
            val response = apolloClient.query(EpisodeQuery(id)).execute()
            response.data?.episode?.toEpisodeModelDetails()?.let { emit(it) }
                ?: throw NetworkException.ClientNetworkException
        }
    }
}

