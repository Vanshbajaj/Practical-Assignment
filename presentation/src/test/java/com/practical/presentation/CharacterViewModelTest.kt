import com.practical.domain.CharacterModel
import com.practical.domain.ResultState
import com.practical.domain.usecases.GetCharactersUseCase
import com.practical.presentation.viewmodel.CharacterViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterViewModelTest {

    private lateinit var viewModel: CharacterViewModel
    private lateinit var getCharactersUseCase: GetCharactersUseCase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        getCharactersUseCase = mockk()
        Dispatchers.setMain(testDispatcher)
        viewModel = CharacterViewModel(getCharactersUseCase, testDispatcher)
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when fetchCharacters is called, it should emit Loading state first`() = runTest {
        // Act
        viewModel.fetchCharacters()

        // Assert
        val state = viewModel.charactersState.value
        assertTrue(state is ResultState.Loading)
    }

    @Test
    fun `when fetching characters successfully, it should emit Success state`() = runTest {
        // Arrange
        val expectedCharacters = listOf(
            CharacterModel("Character 1", "Test", "Male", "Single", "www.google.com", "US", "America"),
            CharacterModel("Character 2", "Test1", "Female", "Single", "www.google.com", "Germany", "US")
        )
        coEvery { getCharactersUseCase.invoke() } returns flowOf(ResultState.Success(expectedCharacters))

        // Act
        viewModel.fetchCharacters()

        // Advance the coroutine to trigger state updates
        advanceUntilIdle()

        // Assert
        val state = viewModel.charactersState.value
        assertTrue(state is ResultState.Success)
        assertEquals(expectedCharacters, (state as ResultState.Success).data)
    }

    @Test
    fun `when fetching characters fails, it should emit Error state`() = runTest {
        // Arrange
        val exception = Exception("Network Error")
        coEvery { getCharactersUseCase.invoke() } returns flowOf(ResultState.Error(exception))

        // Act
        viewModel.fetchCharacters()

        // Advance the coroutine to trigger state updates
        advanceUntilIdle()

        // Assert
        val state = viewModel.charactersState.value
        assertTrue(state is ResultState.Error)
        assertEquals(exception, (state as ResultState.Error).exception)
    }
}
