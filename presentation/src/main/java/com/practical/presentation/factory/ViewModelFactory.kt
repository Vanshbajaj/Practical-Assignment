package com.practical.presentation.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practical.domain.usecases.GetCharacterByIdUseCase
import com.practical.domain.usecases.GetCharactersUseCase
import com.practical.domain.usecases.GetEpisodeUseCases
import com.practical.presentation.di.IoDispatcher
import com.practical.presentation.viewmodel.CharacterDetailsViewModel
import com.practical.presentation.viewmodel.CharacterViewModel
import com.practical.presentation.viewmodel.EpisodeDetailsViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase,
    private val getEpisodeUseCases: GetEpisodeUseCases,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
) : ViewModelProvider.Factory {
    private var episodeId: String? = null

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CharacterViewModel::class.java) -> {
                CharacterViewModel(getCharactersUseCase, coroutineDispatcher) as T
            }

            modelClass.isAssignableFrom(CharacterDetailsViewModel::class.java) -> {
                CharacterDetailsViewModel(getCharacterByIdUseCase) as T
            }

            modelClass.isAssignableFrom(EpisodeDetailsViewModel::class.java) -> {
                // Ensure the episodeId is set before creating the ViewModel
                requireNotNull(episodeId) { "Episode ID is missing" }
                EpisodeDetailsViewModel(
                    episodeId ?: "",
                    getEpisodeUseCases,
                    coroutineDispatcher
                ) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }


    fun createEpisodeViewModel(episodeId: String): EpisodeDetailsViewModel {
        this.episodeId = episodeId
        return create(EpisodeDetailsViewModel::class.java)
    }


}
