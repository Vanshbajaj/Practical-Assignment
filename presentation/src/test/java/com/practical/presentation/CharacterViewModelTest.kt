package com.practical.presentation

import app.cash.turbine.test
import com.practical.domain.CharactersListModel
import com.practical.domain.ResultState
import com.practical.domain.usecases.GetCharacterUseCase
import com.practical.domain.usecases.GetCharactersUseCase
import com.practical.presentation.viewmodel.CharacterViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


@ExperimentalCoroutinesApi
class CharacterViewModelTest {
    private lateinit var viewModel: CharacterViewModel
    private lateinit var getCharactersUseCase: GetCharactersUseCase
    private lateinit var getCharacterUseCase: GetCharacterUseCase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        getCharactersUseCase = mockk(relaxed = true)
        getCharacterUseCase = mockk()
        viewModel = CharacterViewModel(getCharactersUseCase, testDispatcher)
    }

    @Test
    fun `given the ViewModel is initialized, when fetching characters, then it should emit loading state initially`() {
        // Given
        // ViewModel is initialized
        // Act


        // When
        val currentState = viewModel.charactersState.value

        // Then
        assertEquals(ResultState.Loading, currentState)
        val state = viewModel.charactersState.value
        assertTrue(state is ResultState.Loading)
    }


    @Test
    fun `when fetching characters, then it should emit success state`() = runTest(testDispatcher) {
        // Arrange
        val expectedCharacters = listOf(
            CharactersListModel(
                "Character 1",
                "Test",
                "Male"
            ),
            CharactersListModel(
                "Character 2",
                "Test1",
                "Female",
            )
        )

        coEvery { getCharactersUseCase.invoke() } returns flowOf(
            ResultState.Success(
                expectedCharacters
            )
        )

        // Act
        viewModel.fetchCharacters()

        // Assert
        viewModel.charactersState.test {
            assertTrue(awaitItem() is ResultState.Loading)
            assertEquals(expectedCharacters, (awaitItem() as ResultState.Success).data)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given the use case returns an error, when fetching characters, then it should emit error state`() =
        runTest(testDispatcher) {
            // Given
            val exception = RuntimeException("Error fetching characters")
            coEvery { getCharactersUseCase.invoke() } returns flow {
                emit(ResultState.Error(exception))
            }
            // When
            viewModel.fetchCharacters()

            // Then
            viewModel.charactersState.test {
                assertTrue(awaitItem() is ResultState.Loading)
                assertEquals(ResultState.Error(exception), awaitItem())
            }

        }
}

