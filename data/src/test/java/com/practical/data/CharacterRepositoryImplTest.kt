package com.practical.data

import app.cash.turbine.test
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import com.data.graphql.CharactersListQuery
import com.practical.data.repository.CharacterRepositoryImpl
import com.practical.domain.ResultState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


@ExperimentalCoroutinesApi
@OptIn(ExperimentalCoroutinesApi::class)
class CharacterRepositoryImplTest {
    private val apolloClient: ApolloClient = mockk(relaxed = true)
    private lateinit var repository: CharacterRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = CharacterRepositoryImpl(apolloClient)
    }

    @Test
    fun `given GraphQL errors, when getCharacters is called, then emits loading followed by error`() =
        runTest {
            // Mock the Apollo Client
            val apolloClient: ApolloClient = mockk(relaxed = true)

            val response: ApolloResponse<CharactersListQuery.Data> = mockk {
                every { hasErrors() } returns true
                //every { errors } returns listOf(mockk()) // Ensure this is a valid mock
            }
            coEvery { apolloClient.query(CharactersListQuery()).execute() } returns response

            // Create the repository with the mocked Apollo client
            val repository = CharacterRepositoryImpl(apolloClient)

            // When: The getCharacters function is called
            repository.getCharacters().test {
                // Then: Assert that the first emitted state is Loading
                assertEquals(ResultState.Loading, awaitItem())
                // Then: Assert that the next emitted state is Error
                val actualErrorState = awaitItem()
                assertTrue(actualErrorState is ResultState.Error)
                assertTrue(actualErrorState.toString().contains("GraphQL errors:"))
                // Then: Ensure there are no more emissions
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given an unexpected exception, when getCharacters is called, then emits loading followed by error`() =
        runTest {
            // Given: An unexpected exception to be thrown
            val expectedException = Exception("Network error")
            // Mock the Apollo client to throw the exception
            coEvery {
                apolloClient.query(CharactersListQuery()).execute()
            } throws expectedException
            // Create the repository with the mocked Apollo client
            val repositoryWithMockApolloClient = CharacterRepositoryImpl(apolloClient)
            // When: The getCharacters function is called
            repositoryWithMockApolloClient.getCharacters().test {
                // Then: Assert that the first emitted state is Loading
                assertEquals(ResultState.Loading, awaitItem())
                assertEquals(
                    ResultState.Error(expectedException),
                    awaitItem()
                )

                cancelAndIgnoreRemainingEvents()
            }
        }


}
