package com.practical.data

import app.cash.turbine.test
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import com.data.graphql.CharactersQuery
import com.practical.common.Constants
import com.practical.data.repository.CharacterRepositoryImpl
import com.practical.domain.ResultState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


@ExperimentalCoroutinesApi
@OptIn(ExperimentalCoroutinesApi::class)
class CharacterRepositoryImplTest {
    private lateinit var apolloClient: ApolloClient
    private  var apolloClientMockk: ApolloClient= mockk(relaxed = true)
    private lateinit var repository: CharacterRepositoryImpl
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockWebServer = MockWebServer()
        mockWebServer.start()
        val baseUrl = mockWebServer.url(Constants.RICK_MORTY_URL).toString()
        apolloClient = ApolloClient.Builder()
            .serverUrl(baseUrl)
            .build()
        repository = CharacterRepositoryImpl(apolloClient)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getCharacters should emit Loading, Success when data is returned from Rick and Morty API`() =
        runTest {
            // When: Collect emissions from the flow using Turbine
            repository.getCharacters().test {
                // First, the flow should emit Loading
                val loadingState = awaitItem()
                assertTrue(loadingState is ResultState.Loading)

                // Then, the flow should emit Success with the character data
                val successState = awaitItem() as ResultState.Success
                assertTrue(successState.data.isNotEmpty())
                assertTrue(successState.data[0].name.isNotEmpty())  // Check if the name is not empty

                // Finally, ensure that no more items are emitted
                awaitComplete()
            }

        }

    @Test
    fun `given GraphQL errors, when getCharacters is called, then emits loading followed by error`() =
        runTest {
            // Mock the Apollo Client
            val response: ApolloResponse<CharactersQuery.Data> = mockk {
                every { hasErrors() } returns true
            }
            coEvery { apolloClientMockk.query(any<CharactersQuery>()).execute() } returns response

            // Create the repository with the mocked Apollo client
            val repository = CharacterRepositoryImpl(apolloClientMockk)

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
                apolloClientMockk.query(any<CharactersQuery>()).execute()
            } throws expectedException
            // Create the repository with the mocked Apollo client
            val repositoryWithMockApolloClient = CharacterRepositoryImpl(apolloClientMockk)
            // When: The getCharacters function is called
            repositoryWithMockApolloClient.getCharacters().test {
                // Then: Assert that the first emitted state is Loading
                assertEquals(ResultState.Loading, awaitItem())
                assertEquals(ResultState.Error(expectedException), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

}
