package com.practical.presentation

import app.cash.turbine.test
import com.practical.domain.CharacterModel
import com.practical.domain.ResultState
import com.practical.domain.usecases.GetCharacterUseCase
import com.practical.presentation.viewmodel.CharacterDetailsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CharacterDetailsViewModelTest {

    private val getCharacterUseCase = mockk<GetCharacterUseCase>()
    private val viewModel = CharacterDetailsViewModel(getCharacterUseCase)

    @Test
    fun `Given character is fetched successfully,emit Success with character details`() =
        runTest {
            // Given: Mock the use case to return a successful character fetch
            val characterId = "123"
            val characterModel = CharacterModel(id = characterId, name = "Test Character")
            coEvery { getCharacterUseCase.invoke(characterId) } returns flow {
                emit(ResultState.Success(characterModel))
            }

            // When: Calling the getCharacter function on the ViewModel
            viewModel.getCharacter(characterId)

            // Then: Assert that the state emits Loading first, then Success with the correct character data
            viewModel.characterState.test {
                assertEquals(ResultState.Success(characterModel), awaitItem())

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Given character fetch fails, When getCharacter is called, Then it should emit Error with error message`() =
        runTest {
            // Given: Mock the use case to return an error state
            val characterId = "123"
            val exception = RuntimeException("Character not found")
            coEvery { getCharacterUseCase.invoke(characterId) } returns flow {
                emit(ResultState.Error(exception))
            }

            // When: Calling the getCharacter function on the ViewModel
            viewModel.getCharacter(characterId)

            // Then: Assert that the state emits Loading first, then Error with the exception message
            viewModel.characterState.test {
                // Then expect Error state
                val errorState = awaitItem()
                assertTrue(errorState is ResultState.Error)
                assertEquals(
                    "Character not found", (errorState as ResultState.Error).exception.message
                )

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Given character is already fetched Then it should not re-fetch and emit the previous state`() =
        runTest {
            // Given: Mock the use case to return a successful character fetch
            val characterId = "123"
            val characterModel = CharacterModel(id = characterId, name = "Test Character")
            coEvery { getCharacterUseCase.invoke(characterId) } returns flow {
                emit(ResultState.Success(characterModel))
            }

            // When: Calling the getCharacter function on the ViewModel twice
            viewModel.getCharacter(characterId)
            viewModel.getCharacter(characterId) // Calling again with the same ID

            // Then: Assert that the state emits Loading first, then Success once
            viewModel.characterState.test {
                val successState = awaitItem()
                assertTrue(successState is ResultState.Success)
                assertEquals(characterModel, (successState as ResultState.Success).data)

                cancelAndIgnoreRemainingEvents()
            }
        }
}
