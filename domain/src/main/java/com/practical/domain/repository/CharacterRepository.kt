package com.practical.domain.repository

import com.practical.domain.CharacterModel
import com.practical.domain.ResultState
import kotlinx.coroutines.flow.Flow


interface CharacterRepository {
    fun getCharacters(): Flow<ResultState<List<CharacterModel>>>
}
