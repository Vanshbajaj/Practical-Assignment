package com.practical.data

import app.cash.turbine.test
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.ExecutionContext
import com.apollographql.apollo.api.Query
import com.data.graphql.CharactersListQuery
import com.practical.data.repository.CharacterRepositoryImpl
import com.practical.domain.CharacterModel
import com.practical.domain.ResultState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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
    fun `should emit success state with characters when data is fetched successfully`() = runTest(testDispatcher) {
        // Given: Create real instances of Data and its nested fields.

        // Create a mock character
        val character = CharactersListQuery.Result(
            id = "1",
            name = "Rick Sanchez",
            image = "https://example.com/image.png"
        )

        // Create a Characters object with a list of characters
        val characters = CharactersListQuery.Characters(
            results = listOf(character) // List with our mock character
        )

        // Create the actual Data object that holds the characters
        val mockData = CharactersListQuery.Data(
            characters = characters
        )

        // Create a mock Response object with our real mockData
        val mockResponse = mockk<ApolloResponse<CharactersListQuery.Data>>(relaxed = true) // Relaxed mock

        // Mock the hasErrors method to return false (i.e., no errors in this response)
        coEvery { mockResponse.hasErrors() } returns false
        coEvery { mockResponse.data } returns mockData

        // Create a mock ApolloClient
        val apolloClient = mockk<ApolloClient>()

        // Mock the query to return the mockResponse
        coEvery { apolloClient.query(CharactersListQuery()).execute() } returns mockResponse

        // Create the repository with the mocked ApolloClient
        val characterRepository = CharacterRepositoryImpl(apolloClient)

        // When: Call the repository to get characters
        val result = characterRepository.getCharacters()

        // Then: Collect the result and verify the emitted state
        result.test {
            println(awaitItem())
            println(awaitItem())
        }



    }
    @Test
    fun `given GraphQL errors, when getCharacters is called, then emits loading followed by error`() =
        runTest {
            // Mock the Apollo Client
            val apolloClient: ApolloClient = mockk(relaxed = true)

            // Mock the response from Apollo Client
            val response: ApolloResponse<CharactersListQuery.Data> = mockk {
                every { hasErrors() } returns true
                //every { errors } returns listOf(mockk()) // Ensure this is a valid mock
            }

            // Mock the Apollo Client's query method
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
                awaitComplete()
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
