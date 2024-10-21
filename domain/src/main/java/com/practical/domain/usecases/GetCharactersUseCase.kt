package com.practical.domain.usecases

import com.domain.graphql.CharactersQuery
import com.practical.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetCharactersUseCase @Inject constructor(private val repository: CharacterRepository) {
    suspend operator fun invoke(): Flow<List<CharactersQuery.Data>> {
        return repository.getCharacters()
    }
}