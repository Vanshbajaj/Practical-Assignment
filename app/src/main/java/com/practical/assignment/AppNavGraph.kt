package com.practical.assignment

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.practical.presentation.screens.CharacterScreen
import com.practical.presentation.viewmodel.CharacterViewModel
import com.practical.presentation.viewmodel.ViewModelFactory

@Composable
fun AppNavGraph(navController: NavHostController, viewModelFactory: ViewModelFactory) {
    NavHost(navController, startDestination = Home) {
        composable<Home> {
            val characterViewModel: CharacterViewModel =
                ViewModelProvider(LocalContext.current as ComponentActivity, viewModelFactory).get(
                    CharacterViewModel::class.java
                )
            CharacterScreen(characterViewModel)
        }
    }
}
