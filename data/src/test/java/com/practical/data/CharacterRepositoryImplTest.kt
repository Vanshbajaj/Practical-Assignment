package com.practical.data

import app.cash.turbine.test
import com.apollographql.apollo.ApolloClient
import com.data.graphql.CharactersListQuery
import com.data.graphql.EpisodeQuery
import com.practical.common.Constants
import com.practical.data.network.NetworkException
import com.practical.data.repository.CharacterRepositoryImpl
import com.practical.domain.Character
import com.practical.domain.CharacterModel
import com.practical.domain.CharactersListModel
import com.practical.domain.Episode
import com.practical.domain.EpisodeData
import com.practical.domain.EpisodeModel
import com.practical.domain.EpisodeModelDetails
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
        } throws NetworkException.ApolloClientException

        // 2. Test the Flow using Turbine
        repository.getCharactersList().test {
            // Expecting an error
            val error = awaitError()
            assert(error is NetworkException.ApolloClientException)
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

        // Simulate ApolloClient throwing an exception when making the request
        coEvery {
            apolloClient.query(any<CharactersListQuery>()).execute()
        } throws NetworkException.ApolloClientException

        // When & Then
        repository.getCharacter(characterId).test {
            // Expect the flow to throw an error
            val error = awaitError() // Capture the error emitted by the flow
            // Assert the error is an instance of ApolloNetworkException
            assert(error is NetworkException.ApolloClientException)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit episode details when query is successful`() = runTest {
        // Given
        val mockResponseBody = """
    {
      "data": {
        "episode": {
          "name": "Pilot",
          "air_date": "December 2, 2013",
          "characters": [
            {"id": "1", "image": "https://rickandmortyapi.com/api/character/avatar/1.jpeg"},
            {"id": "2", "image": "https://rickandmortyapi.com/api/character/avatar/2.jpeg"},
            {"id": "35", "image": "https://rickandmortyapi.com/api/character/avatar/35.jpeg"},
            {"id": "38", "image": "https://rickandmortyapi.com/api/character/avatar/38.jpeg"},
            {"id": "62", "image": "https://rickandmortyapi.com/api/character/avatar/62.jpeg"},
            {"id": "92", "image": "https://rickandmortyapi.com/api/character/avatar/92.jpeg"},
            {"id": "127", "image": "https://rickandmortyapi.com/api/character/avatar/127.jpeg"},
            {"id": "144", "image": "https://rickandmortyapi.com/api/character/avatar/144.jpeg"},
            {"id": "158", "image": "https://rickandmortyapi.com/api/character/avatar/158.jpeg"},
            {"id": "175", "image": "https://rickandmortyapi.com/api/character/avatar/175.jpeg"},
            {"id": "179", "image": "https://rickandmortyapi.com/api/character/avatar/179.jpeg"},
            {"id": "181", "image": "https://rickandmortyapi.com/api/character/avatar/181.jpeg"},
            {"id": "239", "image": "https://rickandmortyapi.com/api/character/avatar/239.jpeg"},
            {"id": "249", "image": "https://rickandmortyapi.com/api/character/avatar/249.jpeg"},
            {"id": "271", "image": "https://rickandmortyapi.com/api/character/avatar/271.jpeg"},
            {"id": "338", "image": "https://rickandmortyapi.com/api/character/avatar/338.jpeg"},
            {"id": "394", "image": "https://rickandmortyapi.com/api/character/avatar/394.jpeg"},
            {"id": "395", "image": "https://rickandmortyapi.com/api/character/avatar/395.jpeg"},
            {"id": "435", "image": "https://rickandmortyapi.com/api/character/avatar/435.jpeg"}
          ]
        }
      }
    }
    """.trimIndent()

        val mockResponse = MockResponse().setResponseCode(200).setBody(mockResponseBody)
        mockWebServer.enqueue(mockResponse)

        // Generate the list of characters dynamically
        val characters = listOf(
            "1", "2", "35", "38", "62", "92", "127", "144", "158", "175", "179", "181",
            "239", "249", "271", "338", "394", "395", "435"
        ).map { id ->
            Character(id = id, image = "https://rickandmortyapi.com/api/character/avatar/$id.jpeg")
        }

        // When
        repositoryWebServer.getEpisodeId("1").test {
            val expectedData = EpisodeModelDetails(
                data = EpisodeData(
                    episode = Episode(
                        name = "Pilot",
                        airDate = "December 2, 2013",
                        characters = characters
                    )
                )
            )

            // Then
            assertEquals(expectedData, awaitItem())
            awaitComplete()
        }
    }



    @Test
    fun `should throw exception when query fails with server error`() = runTest {
        val episodeId = "1"

        // Simulate ApolloClient throwing an exception when making the request
        coEvery {
            apolloClient.query(any<EpisodeQuery>()).execute()
        } throws NetworkException.ApolloClientException

        // When & Then
        repository.getEpisodeId(episodeId).test {
            // Expect the flow to throw an error
            val error = awaitError() // Capture the error emitted by the flow
            // Assert the error is an instance of ApolloNetworkException
            assert(error is NetworkException.ApolloClientException)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
