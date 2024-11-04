package com.practical.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.practical.domain.CharacterModel
import com.practical.domain.ResultState
import com.practical.domain.usecases.GetCharactersUseCase
import com.practical.presentation.viewmodel.CharacterViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class CharacterViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CharacterViewModel
    private lateinit var getCharactersUseCase: GetCharactersUseCase


    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        getCharactersUseCase = mockk() // Create the mock
        Dispatchers.setMain(testDispatcher)
        viewModel = CharacterViewModel(getCharactersUseCase, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given the ViewModel is initialized, when fetching characters, then it should emit loading state initially`() {
        // Given
        // ViewModel is initialized

        // When
        val currentState = viewModel.charactersState.value

        // Then
        assertEquals(ResultState.Loading, currentState)
    }

    @Test
    fun `when fetching characters, then it should emit success state`() = runTest {
        // Arrange
        val expectedCharacters = listOf(
            CharacterModel("Character 1", "Test", "Male", "Single", "www.google.com", "US", "America"),
            CharacterModel("Character 2", "Test1", "Female", "Single", "www.google.com", "Germany", "US")
        )

        coEvery { getCharactersUseCase.invoke() } returns flow {
            emit(ResultState.Loading)
            emit(ResultState.Success(expectedCharacters))
        }

        // Act
        viewModel.fetchCharacters()
        advanceUntilIdle()

        // Assert
        viewModel.charactersState.collect { state ->
            when (state) {
                is ResultState.Success -> {
                    assertEquals(expectedCharacters, state.data)
                    return@collect // Exit once you assert
                }
                is ResultState.Loading -> {
                    // Handle loading state if needed
                }
                is ResultState.Error -> {
                    fail("Expected Success state but got Error")
                }
            }
        }
    }

    @Test
    fun `given the use case returns an error, when fetching characters, then it should emit error state`() =
        runTest {
            // Given
            val exception = RuntimeException("Error fetching characters")
            coEvery { getCharactersUseCase.invoke() } returns flow {
                emit(ResultState.Loading) // Emit loading first
                emit(ResultState.Error(exception)) // Then emit error
            }

            // When
            viewModel.fetchCharacters()
            advanceUntilIdle() // Process any pending emissions

            // Then
            assertEquals(ResultState.Error(exception), viewModel.charactersState.value)
        }

}


