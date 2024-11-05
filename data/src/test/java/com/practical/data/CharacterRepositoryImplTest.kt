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

//    @Test
//    fun `given successful response, when getCharacters is called, then emits loading followed by success`() =
//        runTest {
//            // Given: Mock the GraphQL response
//            val mockCharacter = CharactersQuery.Result(
//                name = "Rick Sanchez",
//                species = "Human",
//                gender = "Male",
//                status = "Alive",
//                image = "image_url",
//                origin = CharactersQuery.Origin(
//                    name = "Earth",
//                    type = "",
//                    id = "",
//                    dimension = "",
//                    created = ""
//                ),
//                location = CharactersQuery.Location(dimension = "Dimension C-137", created = ""),
//                created = "",
//                type = ""
//            )
//
//            val charactersResponse = CharactersQuery.Data(
//                characters = CharactersQuery.Characters(
//                    results = listOf(mockCharacter),
//                    info = CharactersQuery.Info(
//                        count = 1,
//                        pages = 1,
//                        next = null,
//                        prev = null
//                    )
//                )
//            )
//
//            // Mock the ApolloResponse
//            val response: ApolloResponse<CharactersQuery.Data> = mockk(relaxed = true)
//            every { response.hasErrors() } returns false
//           // every { response.data } returns charactersResponse
//
//            // Mock the Apollo Client's query method
//            coEvery { apolloClient.query(any<CharactersQuery>()).execute()} returns response
//
//            // When: The getCharacters function is called
//            repository.getCharacters().test {
//                assertEquals(ResultState.Loading, awaitItem())
//
//                val expectedCharacters = listOf(
//                    CharacterModel(
//                        name = "Rick Sanchez",
//                        species = "Human",
//                        gender = "Male",
//                        status = "Alive",
//                        image = "image_url",
//                        origin = "Earth",
//                        location = "Dimension C-137"
//                    )
//                )
//                assertEquals(ResultState.Success(expectedCharacters), awaitItem())
//
//               cancelAndIgnoreRemainingEvents()
//            }
//        }


    @Test
    fun `given GraphQL errors, when getCharacters is called, then emits loading followed by error`() =
        runTest {
            // Mock the Apollo Client
            val apolloClient: ApolloClient = mockk(relaxed = true)

            // Mock the response from Apollo Client
            val response: ApolloResponse<CharactersQuery.Data> = mockk {
                every { hasErrors() } returns true
                //every { errors } returns listOf(mockk()) // Ensure this is a valid mock
            }

            // Mock the Apollo Client's query method
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
                apolloClient.query(any<CharactersQuery>()).execute()
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


                // Then: Assert that the next emitted state is Error with the expected exception


                // Additional check: Ensure that the exception message is the same as expected


                // Optionally: You can also check that the error type is the same
                // Confirm it's the expected Exception type

                // Then: Ensure there are no more emissions
                cancelAndIgnoreRemainingEvents()
            }
        }


}
