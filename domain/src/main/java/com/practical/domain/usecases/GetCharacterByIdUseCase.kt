package com.practical.domain.usecases

import com.practical.domain.CharacterModel
import com.practical.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow

class GetCharacterByIdUseCase(private val repository: CharacterRepository) {
     operator fun invoke(id: String): Flow<CharacterModel> {
        return repository.getCharacter(id)
    }
}
