package com.practical.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practical.data.network.NetworkException
import com.practical.domain.CharactersListModel
import com.practical.domain.usecases.GetCharactersUseCase
import com.practical.presentation.UiState
import com.practical.presentation.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

class CharacterViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,

    ) : ViewModel() {
    private val _charactersState =
        MutableStateFlow<UiState<List<CharactersListModel>>>(UiState.Loading)
    val charactersState: StateFlow<UiState<List<CharactersListModel>>> = _charactersState

    init {
        fetchCharacters()
    }

    fun fetchCharacters() {
        viewModelScope.launch(coroutineDispatcher) {
            getCharactersUseCase.invoke()
                .catch { _charactersState.emit(UiState.Error(it)) }
                .collect { result ->
                    if (result.isEmpty()) {
                        _charactersState.emit(UiState.Error(NetworkException.ClientNetworkException))
                    } else {
                        _charactersState.emit(UiState.Success(result))
                    }

                }
        }
    }
}
