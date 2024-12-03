package com.practical.presentation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practical.presentation.viewmodel.EpisodeDetailsViewModel
import javax.inject.Inject


class EpisodeDetailsViewModelFactory @Inject constructor(
    private val episodeDetailsFactory: EpisodeDetailsFactory, // Injected assisted factory
) : ViewModelProvider.Factory {

    fun create(episodeId: String): EpisodeDetailsViewModel {
        // Create and return the ViewModel using the assisted factory
        return episodeDetailsFactory.create(episodeId)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EpisodeDetailsViewModel::class.java)) {
            return episodeDetailsFactory.create("") as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


