package com.practical.data

import app.cash.turbine.test
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
    fun `should emit loading and then success when data is fetched`() = runTest {
        // Given: Mock GraphQL response data
        val characterResults = listOf(
            CharactersQuery.Result(
                name = "Rick Sanchez",
                species = "Human",
                gender = "Male",
                status = "Alive",
                image = "image_url",
                origin = CharactersQuery.Origin(
                    name = "Earth",
                    id = "1",
                    type = "Planet",
                    dimension = "C-137",
                    created = "2020-01-01"
                ),
                location = CharactersQuery.Location(
                    dimension = "Dimension C-137",
                    created = "2020-01-01"
                ),
                type = "",
                created = ""
            )
        )

        // Create a mock ApolloResponse with the required data
        val mockResponse: ApolloResponse<CharactersQuery.Data> = mockk()

        // Mock the ApolloClient query execution to return the mock response
        coEvery { apolloClient.query(CharactersQuery()) } returns mockk {
            coEvery { execute() } returns mockResponse
        }

        // Mock the data property to return the expected data
        coEvery { mockResponse.data } returns CharactersQuery.Data(
            characters = CharactersQuery.Characters(
                info = CharactersQuery.Info(count = 1, pages = 1, next = null, prev = null),
                results = characterResults
            )
        )

        coEvery { mockResponse.hasErrors() } returns false
        // When & Then
        repository.getCharacters().test {
            // Assert loading state first
            assertEquals(ResultState.Loading, awaitItem())

            // Assert success state with the transformed characters data
            val expectedCharacters = listOf(
                CharacterModel(
                    name = "Rick Sanchez",
                    species = "Human",
                    gender = "Male",
                    status = "Alive",
                    image = "image_url",
                    origin = "Earth",
                    location = "Dimension C-137"
                )
            )

            // Check the emitted success state
            val result = awaitItem()
            println("Emitted result: $result")  // Log the emitted result for debugging
            assertEquals(ResultState.Success(expectedCharacters), result)

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