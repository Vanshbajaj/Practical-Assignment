package com.practical.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.practical.domain.CharacterModel
import com.practical.domain.ResultState
import com.practical.domain.usecases.GetCharacterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class CharacterDetailsViewModel @Inject constructor(
    private val getCharacterUseCase: GetCharacterUseCase,
) : ViewModel() {
    private val _characterState = MutableStateFlow<ResultState<CharacterModel>>(ResultState.Loading)
    val characterState: StateFlow<ResultState<CharacterModel>> = _characterState


    suspend fun getCharacter(id: String){
        getCharacterUseCase.invoke(id).collect{collect->
            _characterState.emit(collect)

        }
    }

}
