package com.practical.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practical.domain.CharacterModel
import com.practical.domain.CharactersListModel
import com.practical.domain.ResultState
import com.practical.domain.usecases.GetCharacterUseCase
import com.practical.domain.usecases.GetCharactersUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

class CharacterViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher

) : ViewModel() {

    private val _charactersState =
        MutableStateFlow<ResultState<List<CharactersListModel>>>(ResultState.Loading)
    val charactersState: StateFlow<ResultState<List<CharactersListModel>>> = _charactersState

    init {
        fetchCharacters()
    }

    fun fetchCharacters() {
        viewModelScope.launch(coroutineDispatcher) {
            getCharactersUseCase.invoke()
                .catch { _charactersState.emit(ResultState.Error(it)) }
                .collect { result ->
                    _charactersState.emit(result)
                }
        }
    }
}
