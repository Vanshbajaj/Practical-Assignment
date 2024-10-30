package com.practical.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practical.domain.ResultState
import com.practical.domain.CharacterModel
import com.practical.domain.usecases.GetCharactersUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CharacterViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    private val _charactersState =
        MutableStateFlow<ResultState<List<CharacterModel>>>(ResultState.Loading)
    val charactersState: StateFlow<ResultState<List<CharacterModel>>> get() = _charactersState


    init {
        fetchCharacters()
    }

    fun fetchCharacters() {
        viewModelScope.launch(Dispatchers.IO) { // Emit loading state
            val result = runCatching {
                getCharactersUseCase() // Fetch characters
            }

            result.onSuccess { state ->
                // Ensure that the result is of type Success
                when (state) {
                    is ResultState.Success -> {
                        _charactersState.emit(ResultState.Success(state.data)) // Emit success
                    }
                    is ResultState.Error -> {
                        _charactersState.emit(ResultState.Error(state.exception)) // Emit error
                    }
                    ResultState.Loading -> {
                        _charactersState.emit(ResultState.Loading)
                    }
                }
            }.onFailure { exception ->
                val exception = exception as? Exception ?: Exception(exception) // Cast or wrap
                _charactersState.emit(ResultState.Error(exception))
            }
        }
    }

}

