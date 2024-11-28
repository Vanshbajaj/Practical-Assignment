package com.practical.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.practical.domain.CharacterModel
import com.practical.domain.usecases.GetCharacterUseCase
import com.practical.presentation.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class CharacterDetailsViewModel @Inject constructor(
    private val getCharacterUseCase: GetCharacterUseCase,
) : ViewModel() {
    private var characterId: String? = null
    private val _characterState = MutableStateFlow<UiState<CharacterModel>>(UiState.Loading)
    val characterState: StateFlow<UiState<CharacterModel>> = _characterState

    suspend fun getCharacter(id: String) {
        if (characterId == null) {
            getCharacterUseCase.invoke(id).collect { collect ->
                _characterState.emit(UiState.Success(collect))
                characterId = id
            }
        }
    }
}
