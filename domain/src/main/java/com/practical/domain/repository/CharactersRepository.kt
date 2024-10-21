package com.practical.domain.repository

import com.domain.graphql.CharactersQuery
import kotlinx.coroutines.flow.Flow

interface CharactersRepository {
    suspend fun getCharacters(): Flow<List<CharactersQuery.Data>>
}