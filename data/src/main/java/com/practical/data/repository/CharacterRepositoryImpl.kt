package com.practical.data.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.exception.ApolloNetworkException
import com.apollographql.apollo.exception.DefaultApolloException
import com.apollographql.apollo.exception.RouterError
import com.apollographql.apollo.exception.SubscriptionConnectionException
import com.apollographql.apollo.exception.SubscriptionOperationException
import com.data.graphql.CharacterDetailsQuery
import com.data.graphql.CharactersListQuery
import com.data.graphql.EpisodeQuery
import com.practical.data.network.ApolloClientException
import com.practical.data.network.ClientNetworkException
import com.practical.data.toCharacterModel
import com.practical.data.toEpisodeModelDetails
import com.practical.domain.CharacterModel
import com.practical.domain.CharactersListModel
import com.practical.domain.EpisodeModelDetails
import com.practical.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient,
) : CharacterRepository {

    override fun getCharactersList(): Flow<List<CharactersListModel>> = flow {
        val response = apolloClient.query(CharactersListQuery()).execute()
        try {
            val characters = response.data?.characters?.results?.mapNotNull { character ->
                CharactersListModel(
                    id = character?.id,
                    name = character?.name,
                    image = character?.image,
                )
            } ?: emptyList()
            emit(characters)
        } catch (t: ApolloException) {
            // Handle specific ApolloException cases
            when (t) {
                is ApolloNetworkException -> {
                    throw ClientNetworkException()
                }

                is SubscriptionOperationException, is SubscriptionConnectionException, is RouterError -> {
                    throw ApolloClientException()
                }

                else -> {
                    // Default case for unknown ApolloExceptions
                    throw DefaultApolloException("Unknown Apollo error occurred", t)
                }
            }
        } catch (e: IOException) {
            // Handle any other IOExceptions like network disconnections or timeouts
            throw ApolloNetworkException("General I/O error occurred", e)
        }

    }

    override fun getCharacter(id: String): Flow<CharacterModel> {
        return flow {
            val response = apolloClient.query(CharacterDetailsQuery(id)).execute()
            response.data?.character?.toCharacterModel()?.let {
                emit(it) // Emit character data
            } ?: throw ApolloClientException() // Throw error if no character found
        }.catch { throwable ->
            // Handle and propagate errors from the data layer
            when (throwable) {
                is ApolloNetworkException -> {
                    // Propagate ApolloNetworkException to ViewModel
                    throw ApolloNetworkException("Network error occurred. Please check your internet connection.")
                }
                is ApolloClientException -> {
                    throw ApolloClientException()
                }
                is IOException -> {
                    throw ApolloNetworkException()
                }
                else -> {
                    throw ApolloClientException()
                }
            }
        }
    }


    override fun getEpisodeId(id: String): Flow<EpisodeModelDetails> {
        return flow {
            try {
                val response = apolloClient.query(EpisodeQuery(id)).execute()
                response.data?.episode?.toEpisodeModelDetails()?.let { emit(it) }

            } catch (t: ApolloException) {
                // Handle specific ApolloException cases
                when (t) {
                    is ApolloNetworkException -> {
                        throw ClientNetworkException()
                    }

                    is SubscriptionOperationException, is SubscriptionConnectionException, is RouterError -> {
                        throw ApolloClientException()
                    }

                    else -> {
                        // Default case for unknown ApolloExceptions
                        throw DefaultApolloException("Unknown Apollo error occurred", t)
                    }
                }
            } catch (e: IOException) {
                // Handle any other IOExceptions like network disconnections or timeouts
                throw ApolloNetworkException("General I/O error occurred", e)
            }
        }
    }
}
