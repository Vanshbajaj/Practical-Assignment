package com.practical.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practical.domain.usecases.GetCharacterUseCase
import com.practical.domain.usecases.GetCharactersUseCase
import com.practical.presentation.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val getCharacterUseCase: GetCharacterUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CharacterViewModel::class.java) -> {
                CharacterViewModel(getCharactersUseCase,getCharacterUseCase,ioDispatcher) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
