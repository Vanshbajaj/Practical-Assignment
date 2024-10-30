package com.practical.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practical.domain.usecases.GetCharactersUseCase
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CharacterViewModel::class.java) -> {
                CharacterViewModel(getCharactersUseCase) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}