package com.practical.assignment

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.practical.presentation.screens.CharacterListScreen
import com.practical.presentation.screens.CharacterScreen
import com.practical.presentation.viewmodel.CharacterViewModel
import com.practical.presentation.viewmodel.ViewModelFactory

@Composable
fun AppNavGraph(navController: NavHostController, viewModelFactory: ViewModelFactory) {
    NavHost(navController, startDestination = Home) {
        composable<Home> {
            val characterViewModel: CharacterViewModel = viewModel(factory = viewModelFactory)
            CharacterListScreen(characterViewModel) { characterId, characterName ->
                navController.navigate(CharacterScreenData(characterId, characterName))
            }
        }
        composable<CharacterScreenData> { backStackEntry ->
            val screen: CharacterScreenData = backStackEntry.toRoute()
            val characterViewModel: CharacterViewModel = viewModel(factory = viewModelFactory)
            CharacterScreen(screen.id, screen.name, characterViewModel, navController)
        }
    }
}
