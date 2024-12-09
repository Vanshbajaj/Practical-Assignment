package com.practical.domain

import app.cash.turbine.test
import com.practical.domain.repository.CharacterRepository
import com.practical.domain.usecases.GetCharactersUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetCharactersUseCaseTest {

    private val characterRepository = mockk<CharacterRepository>()
    private val getCharactersUseCase = GetCharactersUseCase(characterRepository)

    @Test
    fun `should return Success when characters are retrieved`() = runTest {
        //Given
        val charactersList = listOf(
            CharactersListModel(
                id = "1",
                name = "Character 1",
                image = "http://test.com/image1.png"
            ),
            CharactersListModel(
                id = "2",
                name = "Character 2",
                image = "http://test.com/image2.png"
            )
        )

        // Mock the repository to return the character list
        coEvery { characterRepository.getCharactersList() } returns flowOf(charactersList)

        //Then
        getCharactersUseCase().test {
            val emittedList = awaitItem()

            // Assert that the emitted list is the same as the mock character list
            assertEquals(charactersList, emittedList)

            awaitComplete() // Ensure the flow completes
        }
    }

    @Test
    fun `should return empty list when repository returns empty list`() = runTest {
        // Given
        val emptyList = emptyList<CharactersListModel>()

        // Mock the repository to return an empty list
        coEvery { characterRepository.getCharactersList() } returns flowOf(emptyList)

        // When & Then
        getCharactersUseCase().test {
            // Collect the emitted list from the flow
            val emittedList = awaitItem()

            // Assert that the emitted list is empty
            assertTrue(emittedList.isEmpty())

            cancelAndIgnoreRemainingEvents()
        }
    }
}

