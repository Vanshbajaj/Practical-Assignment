package com.practical.domain.repository

import com.practical.domain.CharacterModel

interface CharacterRepository {
    suspend fun getCharacters(): List<CharacterModel>

}
