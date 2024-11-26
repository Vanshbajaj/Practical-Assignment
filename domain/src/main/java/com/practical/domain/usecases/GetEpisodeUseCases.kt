package com.practical.domain.usecases

import com.practical.domain.EpisodeModelDetails
import com.practical.core.ResultState
import com.practical.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow

class GetEpisodeUseCases(private val repository: CharacterRepository) {
    operator fun invoke(id: String): Flow<com.practical.core.ResultState<EpisodeModelDetails>> {
        return repository.getEpisodeId(id)
    }
}
