package com.practical.data

import app.cash.turbine.test
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import com.data.graphql.CharactersQuery
import com.practical.data.repository.CharacterRepositoryImpl
import com.practical.domain.CharacterModel
import com.practical.domain.ResultState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
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
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = CharacterRepositoryImpl(apolloClient)
    }


    @Test
    fun `getCharacters should emit Success state with characters when data is fetched successfully`() =
        runTest(testDispatcher) {
            val mockCharacters = listOf(
                CharacterModel(
                    name = "Rick Sanchez",
                    species = "Human",
                    gender = "Male",
                    status = "Alive",
                    image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                    origin = "Earth (Dimension C-137)",
                    location = "Earth (Dimension C-137)"
                )
            )
            val mockResponse = mockk<ApolloResponse<CharactersQuery.Data>>(relaxed = true)
            // Mock the ApolloCall to return the mockResponse directly
            val mockCall = mockk<ApolloCall<CharactersQuery.Data>>(relaxed = true)
            coEvery { mockCall.execute() } returns mockResponse
            every { apolloClient.query<CharactersQuery.Data>(any()) } returns mockCall

           repository.getCharacters().test {
                assertEquals(ResultState.Loading,awaitItem())
               println(awaitItem())
               cancelAndIgnoreRemainingEvents()
            }
        }


    @Test
    fun `given GraphQL errors, when getCharacters is called, then emits loading followed by error`() =
        runTest {
            // Mock the Apollo Client
            val response: ApolloResponse<CharactersQuery.Data> = mockk {
                every { hasErrors() } returns true
            }
            coEvery { apolloClient.query(any<CharactersQuery>()).execute() } returns response

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
            val expectedException = Exception("Network error")
            coEvery {
                apolloClient.query(any<CharactersQuery>()).execute()
            } throws expectedException
            // Create the repository with the mocked Apollo client
            val repositoryWithMockApolloClient = CharacterRepositoryImpl(apolloClient)
            // When: The getCharacters function is called
            repositoryWithMockApolloClient.getCharacters().test {
                // Then: Assert that the first emitted state is Loading
                assertEquals(ResultState.Loading, awaitItem())
                assertEquals(ResultState.Error(expectedException), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
}