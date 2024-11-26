package com.practical.domain

import app.cash.turbine.test
import com.practical.core.ResultState
import com.practical.domain.repository.CharacterRepository
import com.practical.domain.usecases.GetCharactersUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetCharactersUseCaseTest {

    private val characterRepository = mockk<CharacterRepository>()
    private val getCharactersUseCase = GetCharactersUseCase(characterRepository)

    @Test
    fun `should return Loading state when fetching characters`() = runTest {
        //Given
        coEvery { characterRepository.getCharactersList() } returns flow {
            emit(ResultState.Loading)
            //When
            emit(
                ResultState.Success(
                    listOf(
                        CharactersListModel(id = "1", name = "Character One")
                    )
                )
            )
        }

        //Then
        getCharactersUseCase().test {
            // Collecting the first result (Loading)
            assertTrue(awaitItem() is ResultState.Loading)

            // Collecting the second result (Success)
            assertTrue(awaitItem() is ResultState.Success)
            awaitComplete() // Ensure the flow completes
        }
    }


    @Test
    fun `should return Success when characters are retrieved`() = runTest {
        //Given
        val characterList = listOf(
            CharactersListModel(id = "1", name = "Character One"),
            CharactersListModel(id = "2", name = "Character Two")
        )

        coEvery { characterRepository.getCharactersList() } returns flow {
            emit(ResultState.Loading)
            emit(ResultState.Success(characterList))
        }


        getCharactersUseCase().test {
            // Collecting the result emitted by the flow
            assertEquals(ResultState.Loading, awaitItem())
            assertEquals(characterList, (awaitItem() as ResultState.Success).data)
            awaitComplete()
        }
    }

    @Test
    fun `should return Error when an exception occurs`() = runTest {
        //Given
        val exception = RuntimeException("Error fetching characters")
        coEvery { characterRepository.getCharactersList() } returns flow {
            emit(ResultState.Loading)
            emit(ResultState.Error(exception))
        }

        //When
        getCharactersUseCase().test {
            // Collecting the result emitted by the flow
            assertTrue(awaitItem() is ResultState.Loading)
            assertEquals(
                "Error fetching characters",
                (awaitItem() as ResultState.Error).exception.message
            )
            awaitComplete() //Then
        }
    }
}
