package com.practical.domain.repository


import com.domain.graphql.CharactersQuery

interface CharacterRepository {
    suspend fun getCharacters(): List<CharactersQuery.Data>
}