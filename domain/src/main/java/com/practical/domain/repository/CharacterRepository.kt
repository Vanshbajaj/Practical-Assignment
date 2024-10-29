package com.practical.domain.repository

import com.practical.domain.CharacterDataClass

interface CharacterRepository {
    suspend fun getCharacters(): List<CharacterDataClass>
}