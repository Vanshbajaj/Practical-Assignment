package com.practical.presentation

import app.cash.turbine.test
import com.practical.domain.Episode
import com.practical.domain.EpisodeData
import com.practical.domain.EpisodeModelDetails
import com.practical.domain.usecases.GetEpisodeUseCases
import com.practical.presentation.viewmodel.EpisodeDetailsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class EpisodeDetailsViewModelTest {
    private val episodeUseCases: GetEpisodeUseCases = mockk(relaxed = true)
    private lateinit var viewModel: EpisodeDetailsViewModel
    private val episodeId = "1"
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        // Initialize the ViewModel with the mocked dependencies
        viewModel = EpisodeDetailsViewModel(
            episodeId = episodeId,
            episodeUseCases = episodeUseCases,
            coroutineDispatcher = testDispatcher
        )
    }


    @Test
    fun `should emit loading,`() = runTest(testDispatcher) {
        // Given
        // When & Then: Test the flow emissions
        viewModel.episodeState.test {
            // Assert initial loading state
            assertEquals(UiState.Loading, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when fetching episode, then it should emit success state`() = runTest(testDispatcher) {
        // Arrange
        val expectedResponse = EpisodeModelDetails(
            data = EpisodeData(
                Episode(
                    name = "Pilot",
                    airDate = "December 2, 2013",
                    characters = listOf()
                )
            )

        )


        coEvery { episodeUseCases.invoke(episodeId) } returns flowOf(
            expectedResponse
        )

        // Act
        viewModel.getEpisode()

        // Assert
        viewModel.episodeState.test {
            assertTrue(awaitItem() is UiState.Loading)
            assertEquals(expectedResponse.data.episode, (awaitItem() as UiState.Success).data)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit loading, error states when episode fetching fails`() =
        runTest(testDispatcher) {
            // Given: Mocking an error scenario
            val exception = RuntimeException("Network Error")

            // Mock the repository to throw an exception
            coEvery { episodeUseCases.invoke(episodeId) } returns flow {
                throw exception
            }
            // When
            viewModel.getEpisode()
            //  Then: Test the flow emissions
            viewModel.episodeState.test {
                // Assert initial loading state
                assertEquals(UiState.Loading, awaitItem())

                // Assert error state with the exception
                val errorState = awaitItem()
                assertEquals(exception, (errorState as UiState.Error).exception)
                cancelAndIgnoreRemainingEvents()
            }
        }
}
