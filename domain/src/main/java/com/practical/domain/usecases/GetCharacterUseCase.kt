package com.practical.domain.usecases

import com.practical.domain.CharacterModel
import com.practical.domain.ResultState
import com.practical.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow

class GetCharacterUseCase(private val repository: CharacterRepository) {
    suspend fun getCharacters(id: String): Flow<ResultState<CharacterModel>> {
        return repository.getCharacter(id)
    }
}