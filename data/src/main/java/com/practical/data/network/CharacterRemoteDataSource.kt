package com.practical.data.network

import com.practical.domain.CharacterDataClass

interface CharacterRemoteDataSource {
    suspend fun getCharacters(): List<CharacterDataClass>
}