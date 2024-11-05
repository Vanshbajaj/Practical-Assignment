package com.practical.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practical.domain.CharacterModel
import com.practical.domain.ResultState
import com.practical.domain.usecases.GetCharactersUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class CharacterViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _charactersState =
        MutableStateFlow<ResultState<List<CharacterModel>>>(ResultState.Loading)
    val charactersState: StateFlow<ResultState<List<CharacterModel>>> = _charactersState


    init {
        fetchCharacters()
    }

     fun fetchCharacters() {
        viewModelScope.launch(defaultDispatcher) {
            getCharactersUseCase.invoke()
                .onStart { _charactersState.emit(ResultState.Loading) }
                .catch { _charactersState.emit(ResultState.Error(it)) }
                .collect { result ->
                    _charactersState.emit(result)
                }
        }
    }
}
