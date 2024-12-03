package com.practical.presentation.di

import com.practical.presentation.viewmodel.EpisodeDetailsViewModel
import dagger.assisted.AssistedFactory

@AssistedFactory
interface EpisodeDetailsFactory {
    fun create(episodeId: String): EpisodeDetailsViewModel
}
