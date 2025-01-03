package com.practical.assignment


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.practical.presentation.factory.ViewModelFactory
import com.practical.presentation.screens.CharacterListScreen
import com.practical.presentation.screens.CharacterScreen
import com.practical.presentation.screens.EpisodeDetails
import com.practical.presentation.viewmodel.CharacterDetailsViewModel
import com.practical.presentation.viewmodel.CharacterViewModel
import com.practical.presentation.viewmodel.EpisodeDetailsViewModel


fun ViewModelFactory.episodeViewModelFactory(episodeId: String): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            // Provide the custom creation logic using the episodeId
            return createEpisodeViewModel(episodeId) as T
        }
    }
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    viewModelFactory: ViewModelFactory,
) {
    NavHost(navController, startDestination = Home) {
        composable<Home> {
            val characterViewModel: CharacterViewModel = viewModel(factory = viewModelFactory)
            CharacterListScreen(characterViewModel) { characterId ->
                navController.navigate(CharacterScreenData(characterId))
            }
        }
        composable<CharacterScreenData> { backStackEntry ->
            val screen: CharacterScreenData = backStackEntry.toRoute()
            val characterViewModel: CharacterDetailsViewModel =
                viewModel(factory = viewModelFactory)
            CharacterScreen(screen.id, characterViewModel) { episodeId ->
                navController.navigate(EpisodeScreenData(episodeId))
            }
        }
        composable<EpisodeScreenData> { backStackEntry ->
            val screen: EpisodeScreenData = backStackEntry.toRoute()
            val episodeId = screen.id
            val episodeDetailsViewModel: EpisodeDetailsViewModel = viewModel(
                factory = viewModelFactory.episodeViewModelFactory(episodeId)
            )
            // Display the Episode Details
            EpisodeDetails(episodeDetailsViewModel.episodeState, modifier = Modifier)
        }
    }
}
