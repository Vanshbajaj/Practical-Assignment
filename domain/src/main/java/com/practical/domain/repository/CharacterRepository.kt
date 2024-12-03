package com.practical.domain.repository

import com.practical.domain.CharacterModel
import com.practical.domain.CharactersListModel
import com.practical.domain.EpisodeModelDetails
import com.practical.domain.ResultState
import kotlinx.coroutines.flow.Flow


interface CharacterRepository {
    fun getCharactersList(): Flow<ResultState<List<CharactersListModel>>>
    fun getCharacter(id: String): Flow<ResultState<CharacterModel>>
    fun getEpisodeId(id: String): Flow<ResultState<EpisodeModelDetails>>
}
