package com.practical.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practical.domain.CharacterModel
import com.practical.domain.ResultState
import com.practical.domain.usecases.GetCharacterUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CharacterDetailsViewModel @Inject constructor(
    private val getCharacterUseCase: GetCharacterUseCase,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _characterState = MutableStateFlow<ResultState<CharacterModel>>(ResultState.Loading)
    val characterState: StateFlow<ResultState<CharacterModel>> = _characterState


        fun getCharacter(id: String) {
        viewModelScope.launch(coroutineDispatcher) {
            getCharacterUseCase.invoke(id).collect { result ->
                _characterState.emit(result)
            }
        }
    }



}