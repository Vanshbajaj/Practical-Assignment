package com.practical.data

import app.cash.turbine.test
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.exception.ApolloNetworkException
import com.data.graphql.CharactersListQuery
import com.practical.common.Constants
import com.practical.data.network.NetworkException
import com.practical.data.repository.CharacterRepositoryImpl
import com.practical.domain.CharacterModel
import com.practical.domain.CharactersListModel
import com.practical.domain.EpisodeModel
import com.practical.domain.OriginModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CharacterRepositoryImplTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apolloClientWebServer: ApolloClient
    private val apolloClient: ApolloClient = mockk(relaxed = true)
    private val repository: CharacterRepositoryImpl = CharacterRepositoryImpl(apolloClient)
    private lateinit var repositoryWebServer: CharacterRepositoryImpl


    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        // Create Apollo Client using the MockWebServer's URL
        apolloClientWebServer =
            ApolloClient.Builder().serverUrl(mockWebServer.url(Constants.RICK_MORTY_URL).toString())
                .build()

        repositoryWebServer = CharacterRepositoryImpl(apolloClientWebServer)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getCharactersList should emit Loading, then Success with limited character list data`() =
        runTest {
            // Mock GraphQL response data with only two characters as expected
            val mockResponse = """
        {
            "data": {
                "characters": {
                    "results": [
                        {
                            "id": "1",
                            "name": "Rick Sanchez",
                            "image": "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
                        },
                        {
                            "id": "2",
                            "name": "Morty Smith",
                            "image": "https://rickandmortyapi.com/api/character/avatar/2.jpeg"
                        }
                    ]
                }
            }
        }
        """.trimIndent()

            // Enqueue the mock response to the mock server
            mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(mockResponse))

            // Test the repository method
            repositoryWebServer.getCharactersList().test {
                // Expect Success with the correct list of characters (only two characters in the mock data)
                val expectedList = listOf(
                    CharactersListModel(
                        id = "1",
                        name = "Rick Sanchez",
                        image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
                    ), CharactersListModel(
                        id = "2",
                        name = "Morty Smith",
                        image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg"
                    )
                )
                // Filter the actual data to make sure only the first two characters are checked
                val filteredData = awaitItem().take(2)
                assertEquals(expectedList, filteredData)
                cancelAndIgnoreRemainingEvents()
            }
        }


    @Test
    fun `should return error state when server response contains errors`() = runTest {
        coEvery {
            apolloClient.query(CharactersListQuery()).execute()
        } throws ApolloNetworkException("Server Error")

        // 2. Test the Flow using Turbine
        repository.getCharactersList().test {
            // Expecting an error
            val error = awaitError()
            assert(error is ApolloNetworkException)
            cancelAndIgnoreRemainingEvents()
        }
    }


    /******************************   get Character By Id  ***************************************************/


    @Test
    fun `character data for when ApolloClient query is successful`() = runTest {
        // Given
        val characterId = "6"
        val characterModel = CharacterModel(
            name = "Abadango Cluster Princess",
            status = "Alive",
            species = "Alien",
            gender = "Female",
            origin = OriginModel(
                name = "Abadango",
                dimension = "unknown",
            ),
            image = "https://rickandmortyapi.com/api/character/avatar/6.jpeg",
            episodes = listOf(
                EpisodeModel(
                    id = "27",
                    name = "Rest and Ricklaxation",
                )
            )
        )

        // Set up the mock GraphQL response with more episodes, but we want only the first episode for comparison
        val mockGraphQLResponse = """
    {
      "data": {
        "character": {
      
          "name": "Abadango Cluster Princess",
          "status": "Alive",
          "species": "Alien",
          "gender": "Female",
          "origin": {
            "name": "Abadango"
          },
          "image": "https://rickandmortyapi.com/api/character/avatar/6.jpeg",
          "episodes": [
            {
              "name": "Rest and Ricklaxation",
              "episode": "S03E06"
            }
          ]
        }
      }
    }
""".trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(mockGraphQLResponse).setResponseCode(200))
        //When
        repositoryWebServer.getCharacter(characterId).test {
            //Then
            assertEquals((characterModel), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should return error state when server response contains errors for id`() = runTest {
        // Given
        val characterId = "2"
        val mockErrorMessage = "NO Data"

        // Mock the server response to simulate an error
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setBody("{ \"errors\": [{\"message\": \"$mockErrorMessage\"}]}") // Simulating error response
                .addHeader("Content-Type", "application/json")
        )

        // Simulate ApolloClient throwing an exception when making the request
        coEvery {
            apolloClient.query(any<CharactersListQuery>()).execute()
        } throws ApolloNetworkException("GraphQL Error: $mockErrorMessage")

        // When & Then
        repository.getCharacter(characterId).test {
            // Expect the flow to throw an error
            val error = awaitError() // Capture the error emitted by the flow
            // Assert the error is an instance of ApolloNetworkException
            assert(error is ApolloNetworkException)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
