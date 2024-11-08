package com.practical.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practical.domain.CharacterModel
import com.practical.domain.CharactersListModel
import com.practical.domain.ResultState
import com.practical.domain.usecases.GetCharacterUseCase
import com.practical.domain.usecases.GetCharactersUseCase
import com.practical.presentation.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

class CharacterViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val getCharacterUseCase: GetCharacterUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher

) : ViewModel() {

    private val _charactersState =
        MutableStateFlow<ResultState<List<CharactersListModel>>>(ResultState.Loading)
    val charactersState: StateFlow<ResultState<List<CharactersListModel>>> = _charactersState
    private val _characterState = MutableStateFlow<ResultState<CharacterModel>>(ResultState.Loading)
    val characterState: StateFlow<ResultState<CharacterModel>> = _characterState


    init {
        fetchCharacters()
    }

    fun fetchCharacters() {
        viewModelScope.launch(ioDispatcher) {
            getCharactersUseCase.invoke()
                .catch { _charactersState.emit(ResultState.Error(it)) }
                .collect { result ->
                    _charactersState.emit(result)
                }
        }
    }

    fun getCharacter(id: String) {
        viewModelScope.launch {
            getCharacterUseCase.getCharacters(id).collect { result ->
                _characterState.emit(result)
            }
        }
    }
}

