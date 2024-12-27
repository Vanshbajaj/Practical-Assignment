package com.practical.domain

import app.cash.turbine.test
import com.practical.domain.repository.CharacterRepository
import com.practical.domain.usecases.GetEpisodeUseCases
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetEpisodeUseCasesTest {
    private val characterRepository = mockk<CharacterRepository>()
    private val getEpisodeUseCases = GetEpisodeUseCases(characterRepository)

    @Test
    fun `should emit episode details when repository returns valid data`() = runTest {
        // Given
        val episodeId = "1"
        val mockEpisodeDetails = EpisodeModelDetails(
            data = EpisodeData(
                episode = Episode(
                    name = "Pilot",
                    airDate = "December 2, 2013",
                    characters = listOf(
                        Character(id = "1", image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg")
                    )
                )
            )
        )

        // Mock the repository to return the expected episode details
        coEvery { characterRepository.getEpisodeId(episodeId) } returns flow {
            emit(mockEpisodeDetails)
        }

        // When & Then: Test the flow and ensure the expected data is emitted
        getEpisodeUseCases(episodeId).test {
            val emittedData = awaitItem()  // Get the emitted item from the flow
            assertEquals(mockEpisodeDetails, emittedData)  // Verify the emitted data matches the expected data
            awaitComplete()  // Ensure the flow completes successfully
        }
    }
    @Test
    fun `should emit empty episode details when repository returns empty response`() = runTest {
        // Given
        val episodeId = "2"
        val emptyEpisodeDetails = EpisodeModelDetails(
            data = EpisodeData(
                episode = Episode(
                    name = "",
                    airDate = "",
                    characters = emptyList()
                )
            )
        )

        // Mock the repository to return empty episode details
        coEvery { characterRepository.getEpisodeId(episodeId) } returns flow {
            emit(emptyEpisodeDetails)
        }

        // When & Then: Test the flow and ensure empty data is emitted
        getEpisodeUseCases(episodeId).test {
            val emittedData = awaitItem()
            assertEquals(emptyEpisodeDetails, emittedData)  // Ensure the emitted data matches the empty response
            awaitComplete()
        }
    }
}
