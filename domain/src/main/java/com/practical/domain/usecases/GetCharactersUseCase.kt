package com.practical.domain.usecases

import com.domain.graphql.CharactersQuery
import com.practical.domain.repository.CharacterRepository


class GetCharactersUseCase (private val repository: CharacterRepository) {
    suspend operator fun invoke(): List<CharactersQuery.Data> {
        return repository.getCharacters()
    }
}