package com.practical.domain.repository

import com.practical.domain.CharacterModel
import com.practical.domain.CharactersListModel
import com.practical.domain.EpisodeModelDetails
import com.practical.domain.ResultState
import kotlinx.coroutines.flow.Flow


interface CharacterRepository {
    fun getCharactersList(): Flow<List<CharactersListModel>>
    fun getCharacter(id: String): Flow<CharacterModel>
    fun getEpisodeId(id: String): Flow<EpisodeModelDetails>
}
