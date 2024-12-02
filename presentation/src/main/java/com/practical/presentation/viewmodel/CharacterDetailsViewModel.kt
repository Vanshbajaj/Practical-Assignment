package com.practical.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.practical.data.network.NetworkException
import com.practical.domain.CharacterModel
import com.practical.domain.usecases.GetCharacterByIdUseCase
import com.practical.presentation.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class CharacterDetailsViewModel @Inject constructor(
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase,
) : ViewModel() {
    private var characterId: String? = null
    private val _characterState = MutableStateFlow<UiState<CharacterModel>>(UiState.Loading)
    val characterState: StateFlow<UiState<CharacterModel>> = _characterState

    suspend fun getCharacter(id: String) {
        if (characterId == null) {
            // Emit loading state initially
            _characterState.emit(UiState.Loading)

            // Use `getCharacterUseCase` to fetch data from repository
            getCharacterByIdUseCase.invoke(id)
                .catch {
                    // Emit error state when an exception occurs
                    _characterState.emit(UiState.Error(NetworkException.ClientNetworkException))
                }
                .collect { characterModel ->
                    // On success, emit success state
                    _characterState.emit(UiState.Success(characterModel))
                    characterId = id
                }
        }
    }
}
