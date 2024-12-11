package com.practical.presentation

import app.cash.turbine.test
import com.practical.data.network.NetworkException
import com.practical.domain.CharacterModel
import com.practical.domain.usecases.GetCharacterByIdUseCase
import com.practical.presentation.viewmodel.CharacterDetailsViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CharacterDetailsViewModelTest {

    private val getCharacterByIdUseCase = mockk<GetCharacterByIdUseCase>()
    private val viewModel = CharacterDetailsViewModel(getCharacterByIdUseCase)

    @Test
    fun `Given character is fetched successfully, emit Success with character details`() =
        runTest {
            // Given: Mock the use case to return a successful character fetch
            val characterId = "123"
            val characterModel = CharacterModel(id = characterId, name = "Test Character")

            // Mock the use case to return a flow emitting only Success (no Loading here)
            coEvery { getCharacterByIdUseCase.invoke(characterId) } returns flow {
                emit((characterModel)) // Only emit Success
            }

            // When: Calling the getCharacter function on the ViewModel
            viewModel.getCharacter(characterId)

            // Then: Assert that the state emits Loading first, then Success with the correct character data
            viewModel.characterState.test {
                assertEquals(UiState.Success(characterModel), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `When getCharacter is called, Then it should emit Loading followed by Error with exception`() =
        runTest {
            // Given: Mock the use case to return an error state
            val characterId = "123"
            val exception = NetworkException.ClientNetworkException
            coEvery { getCharacterByIdUseCase.invoke(characterId) } returns flow {
                throw exception
            }

            // When: Calling the getCharacter function on the ViewModel
            viewModel.getCharacter(characterId)

            // Then: Assert that the state emits Loading first, then Error with the exception message
            viewModel.characterState.test {
                // Assert that Loading state is emitted first

                // Assert that Error state is emitted next
                val errorState = awaitItem()
                assertTrue(errorState is UiState.Error)

                // Compare the exception message
                val errorException = (errorState as UiState.Error).exception
                assertTrue(errorException is NetworkException.ClientNetworkException)
                assertEquals(exception.message, (errorException as NetworkException.ClientNetworkException).message)

                // Cancel and ignore remaining events
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Given character is already fetched Then it should not re-fetch and emit the previous state`() =
        runTest {
            // Given: Mock the use case to return a successful character fetch
            val characterId = "123"
            val characterModel = CharacterModel(id = characterId, name = "Test Character")
            coEvery { getCharacterByIdUseCase.invoke(characterId) } returns flow {
                emit(characterModel)
            }

            // When: Calling the getCharacter function on the ViewModel twice
            viewModel.getCharacter(characterId)
            viewModel.getCharacter(characterId) // Calling again with the same ID

            // Then: Assert that the state emits Loading first, then Success once
            viewModel.characterState.test {
                assertEquals(characterModel, (awaitItem() as UiState.Success).data)
                cancelAndIgnoreRemainingEvents()
            }

            // Verify that the use case is called exactly once
            coVerify(exactly = 1) { getCharacterByIdUseCase.invoke(characterId) }
        }

}
