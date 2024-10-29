package com.practical.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practical.domain.CharacterModel
import com.practical.domain.usecases.GetCharactersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CharacterViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    private val _characters = MutableStateFlow<List<CharacterModel>>(emptyList())
    val characters: StateFlow<List<CharacterModel>> get() = _characters

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing

    init {
        fetchCharacters()
    }

    fun fetchCharacters() {
        viewModelScope.launch {
            _isRefreshing.emit(true)
            runCatching {
                getCharactersUseCase()
            }.onSuccess { fetchedCharacters ->
                _characters.emit(fetchedCharacters)
            }.onFailure {
                // Optionally handle the error here
            }.also {
                _isRefreshing.emit(false)
            }
        }
    }
}

