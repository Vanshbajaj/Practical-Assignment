package com.practical.domain

import app.cash.turbine.test
import com.practical.domain.repository.CharacterRepository
import com.practical.domain.usecases.GetCharacterByIdUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GetCharacterByIdUseCaseTest {
    private val characterRepository = mockk<CharacterRepository>(relaxed = true)
    private val getCharacterByIdUseCase = GetCharacterByIdUseCase(characterRepository)

    @Test
    fun `should return character when repository returns valid data`() = runTest {
        // Given
        val characterId = "1"
        val character = CharacterModel(
            name = "Character 1",
            image = "http://example.com/character1.png"
        )

        // Mock the repository to return the character when getCharacter is called
        coEvery { characterRepository.getCharacter(characterId) } returns flowOf(character)

        // When & Then
        getCharacterByIdUseCase(characterId).test {
            // Collect the emitted character from the flow
            val emittedCharacter = awaitItem()

            // Assert that the emitted character matches the expected character
            assertEquals(character, emittedCharacter)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should handle empty flow when no character is found`() = runTest {
        // Given
        val characterId = "999" // An ID that is unlikely to exist

        // Mock the repository to return an empty flow when getCharacter is called
        coEvery { characterRepository.getCharacter(characterId) } returns emptyFlow()

        // When & Then
        getCharacterByIdUseCase(characterId).test {
            // Collect the result from the flow (which should be empty)
            assertNull(null)
            cancelAndIgnoreRemainingEvents()

        }
    }
}
