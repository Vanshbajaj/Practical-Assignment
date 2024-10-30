package com.practical.assignment

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.practical.presentation.CharacterScreen
import com.practical.presentation.viewmodel.CharacterViewModel
import com.practical.presentation.viewmodel.ViewModelFactory

@Composable
fun AppNavGraph(navController: NavHostController, viewModelFactory: ViewModelFactory) {
    NavHost(navController, startDestination = "home") {
        composable("home") {
            val characterViewModel: CharacterViewModel = viewModel(factory = viewModelFactory)
            CharacterScreen(characterViewModel)
        }
    }
}
