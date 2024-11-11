package com.practical.domain.usecases

import com.practical.domain.repository.CharacterRepository


class GetCharactersUseCase(private val repository: CharacterRepository) {
    suspend operator fun invoke() = repository.getCharactersList()
}
