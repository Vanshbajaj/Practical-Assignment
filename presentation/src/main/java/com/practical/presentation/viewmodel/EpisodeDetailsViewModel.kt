package com.practical.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practical.domain.Episode
import com.practical.domain.usecases.GetEpisodeUseCases
import com.practical.presentation.UiState
import com.practical.presentation.di.IoDispatcher
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class EpisodeDetailsViewModel @AssistedInject constructor(
    @Assisted private val episodeId: String,
    private val episodeUseCases: GetEpisodeUseCases,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _episodeState =
        MutableStateFlow<UiState<Episode>>(UiState.Loading)
    val episodeState: StateFlow<UiState<Episode>> = _episodeState

    init {
        getEpisode()
    }

    private fun getEpisode() {
        viewModelScope.launch(coroutineDispatcher) {
            episodeUseCases.invoke(episodeId)
                .catch {
                    _episodeState.emit(UiState.Error(it))
                }
                .collect { result ->
                    _episodeState.emit(UiState.Success(result.data.episode))
                }
        }
    }

}