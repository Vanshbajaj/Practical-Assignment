package com.practical.presentation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practical.presentation.viewmodel.EpisodeDetailsViewModel
import javax.inject.Inject

class EpisodeDetailsViewModelFactory @Inject constructor(
    private val episodeDetailsFactory: EpisodeDetailsFactory, // Injected assisted factory
) : ViewModelProvider.Factory {

    private var episodeId: String? = null

    fun create(episodeId: String): EpisodeDetailsViewModel {
        // Only update the episodeId if it's not already set
        if (this.episodeId == null) {
            this.episodeId = episodeId
        }

        return episodeDetailsFactory.create(episodeId)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EpisodeDetailsViewModel::class.java)) {
            // Ensure the episodeId is set before creating the ViewModel
            requireNotNull(episodeId) { "Episode ID is missing" }
            return episodeDetailsFactory.create(episodeId ?: "") as T
        }
        error("Unknown ViewModel class: ${modelClass.simpleName}")
    }
}
