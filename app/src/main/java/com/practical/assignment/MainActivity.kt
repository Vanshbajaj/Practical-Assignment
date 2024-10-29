package com.practical.assignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.practical.assignment.ui.theme.practicalAssignmentTheme
import com.practical.presentation.characterScreen


import com.practical.presentation.viewmodel.CharacterViewModel
import com.practical.presentation.viewmodel.ViewModelFactory
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: CharacterViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        (application as RickAndMorty).appComponent.inject(this)
        setContent {
            practicalAssignmentTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    characterScreen(viewModel)
                }

            }
        }
    }
}

