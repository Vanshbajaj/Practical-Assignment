package com.practical.domain.repository

import com.practical.domain.CharacterModel
import com.practical.domain.ResultState


interface CharacterRepository {
    suspend fun getCharacters(): ResultState<List<CharacterModel>>
}
