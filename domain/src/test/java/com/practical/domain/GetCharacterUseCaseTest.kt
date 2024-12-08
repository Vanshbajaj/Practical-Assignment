package com.practical.domain

import app.cash.turbine.test
import com.practical.domain.repository.CharacterRepository
import com.practical.domain.usecases.GetCharacterUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetCharacterUseCaseTest {
    private val characterRepository = mockk<CharacterRepository>(relaxed = true)
    private val getCharacterUseCase = GetCharacterUseCase(characterRepository)


    @Test
    fun `should return Loading when character is being fetched`() = runTest {
        // Given
        val characterId = "123"
        coEvery { characterRepository.getCharacter(characterId) } returns flow {
            emit(ResultState.Loading)
            emit(ResultState.Success(CharacterModel(id = characterId, name = "Test Character")))
        }

        // When
        getCharacterUseCase(characterId).test {
            // Collecting the result emitted by the flow
            assertTrue(awaitItem() is ResultState.Loading)
            assertTrue(awaitItem() is ResultState.Success)
            awaitComplete() // Then
        }
    }

    @Test
    fun `should return Success when character is found`() = runTest {
        // Given
        val characterId = "123"
        val characterModel = CharacterModel(id = characterId, name = "Test Character")
        coEvery { characterRepository.getCharacter(characterId) } returns flow {
            emit(ResultState.Success(characterModel))
        }

        // When
        getCharacterUseCase(characterId).test {
            // Collecting the result emitted by the flow
            val result = awaitItem()
            assertTrue(result is ResultState.Success)
            assertEquals(characterModel, (result as ResultState.Success).data)
            awaitComplete() // Then
        }
    }

    @Test
    fun `should return Error when character is not found`() = runTest {
        // Given
        val characterId = "123"
        coEvery { characterRepository.getCharacter(characterId) } returns flow {
            emit(ResultState.Error(RuntimeException("Character not found")))
        }

        // When
        getCharacterUseCase(characterId).test {
            // Collecting the result emitted by the flow
            val result = awaitItem()
            assertTrue(result is ResultState.Error)
            assertEquals("Character not found", (result as ResultState.Error).exception.message)
            awaitComplete() // Then
        }
    }


    @Test
    fun `should return Error when an exception is thrown`() = runTest {
        // Given
        val characterId = "123"
        coEvery { characterRepository.getCharacter(characterId) } returns flow {
            emit(ResultState.Error(RuntimeException("Something went wrong")))
        }

        //When
        getCharacterUseCase(characterId).test {
            // Collecting the result emitted by the flow
            val result = awaitItem()
            assertTrue(result is ResultState.Error)
            assertEquals("Something went wrong", (result as ResultState.Error).exception.message)
            awaitComplete() //Then
        }
    }
}
