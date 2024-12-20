package com.practical.presentation.di

import androidx.lifecycle.ViewModelProvider
import com.practical.presentation.viewmodel.EpisodeDetailsViewModel
import dagger.assisted.AssistedFactory

@AssistedFactory
interface EpisodeDetailsFactory : ViewModelProvider.Factory {
    fun create(episodeId: String): EpisodeDetailsViewModel
}
