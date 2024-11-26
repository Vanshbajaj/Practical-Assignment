package com.practical.data

import app.cash.turbine.test
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import com.data.graphql.CharactersListQuery
import com.practical.common.Constants
import com.practical.data.repository.CharacterRepositoryImpl
import com.practical.domain.CharacterModel
import com.practical.domain.CharactersListModel
import com.practical.domain.EpisodeModel
import com.practical.domain.OriginModel
import com.practical.core.ResultState
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
                // Expect Loading state first
                assertEquals(ResultState.Loading, awaitItem())

                // Expect Success with the correct list of characters (only two characters in the mock data)
                val expectedList = listOf(
                    CharactersListModel(
                        id = "1",
                        name = "Rick Sanchez",
                        image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
                    ),
                    CharactersListModel(
                        id = "2",
                        name = "Morty Smith",
                        image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg"
                    )
                )

                // Await the Success state, and extract the data from ResultState.Success
                val actualResult = awaitItem()
                assertTrue(actualResult is ResultState.Success)
                val actualData = (actualResult as ResultState.Success).data

                // Filter the actual data to make sure only the first two characters are checked
                val filteredData = actualData.take(2)

                // Assert that the actual data matches the expected list
                assertEquals(expectedList, filteredData)

                cancelAndIgnoreRemainingEvents()
            }
        }


    @Test
    fun `given GraphQL errors, when getCharacters is called, then emits loading followed by error`() =
        runTest {
            // Mock the Apollo Client
            val response: ApolloResponse<CharactersListQuery.Data> = mockk {
                every { hasErrors() } returns true
                //every { errors } returns listOf(mockk()) // Ensure this is a valid mock
            }
            coEvery { apolloClient.query(any<CharactersListQuery>()).execute() } returns response

            // Create the repository with the mocked Apollo client


            // When: The getCharacters function is called
            repository.getCharactersList().test {
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
                apolloClient.query(any<CharactersListQuery>()).execute()
            } throws expectedException
            // Create the repository with the mocked Apollo client
            // When: The getCharacters function is called
            repository.getCharactersList().test {
                // Then: Assert that the first emitted state is Loading
                assertEquals(ResultState.Loading, awaitItem())
                assertEquals(ResultState.Error(expectedException), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    /******************************   get Character By Id  ***************************************************/
    @Test
    fun `character data for when ApolloClient query is successful`() = runTest {
        // Given
        val characterId = "6"
        val characterModel = CharacterModel(
            id = "6",
            name = "Abadango Cluster Princess",
            status = "Alive",
            species = "Alien",
            type = "",
            origin = OriginModel(
                id = "2",
                name = "Abadango",
                type = "Cluster",
                dimension = "unknown",
                created = "2017-11-10T13:06:38.182Z"
            ),
            image = "https://rickandmortyapi.com/api/character/avatar/6.jpeg",
            created = "2017-11-04T19:50:28.250Z",
            episodes = listOf(
                EpisodeModel(
                    id = "27",
                    name = "Rest and Ricklaxation",
                    airDate = "August 27, 2017",
                    episode = "S03E06",
                    created = "2017-11-10T12:56:36.515Z"
                )
            )
        )

        // Set up the mock GraphQL response with more episodes, but we want only the first episode for comparison
        val mockGraphQLResponse = """
    {
      "data": {
        "character": {
          "id": "6",
          "name": "Abadango Cluster Princess",
          "status": "Alive",
          "species": "Alien",
          "type": "Princess",
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
            assertEquals(ResultState.Loading, awaitItem())
            assertEquals(ResultState.Success(characterModel), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

    }

    @Test
    fun `should return error state when server response contains errors`() = runTest {
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

        // When & Then
        repository.getCharacter(characterId).test {
            // Assert that the first emitted item is Loading
            assertEquals(ResultState.Loading, awaitItem())

            // Assert that the second emitted item is Error
            val errorState = awaitItem()
            assertTrue(errorState is ResultState.Error)
            assertEquals(mockErrorMessage, (errorState as ResultState.Error).exception.message)

            awaitComplete() // Ensure the flow completes without unconsumed events
        }
    }
}

