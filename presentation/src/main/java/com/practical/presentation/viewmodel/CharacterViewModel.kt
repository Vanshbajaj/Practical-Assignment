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

        private fun fetchCharacters() {
            viewModelScope.launch(Dispatchers.IO) {
                getCharactersUseCase.invoke().collect { result ->
                    _charactersState.emit(result)// Update the state based on the repository result
                }
            }
        }
    }

