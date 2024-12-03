package com.practical.assignment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.practical.presentation.di.EpisodeDetailsViewModelFactory
import com.practical.presentation.factory.ViewModelFactory
import com.practical.presentation.screens.CharacterListScreen
import com.practical.presentation.screens.CharacterScreen
import com.practical.presentation.screens.EpisodeDetails
import com.practical.presentation.viewmodel.CharacterDetailsViewModel
import com.practical.presentation.viewmodel.CharacterViewModel
import com.practical.presentation.viewmodel.EpisodeDetailsViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    viewModelFactory: ViewModelFactory,
    episodeDetailsViewModelFactory: EpisodeDetailsViewModelFactory,
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
            val episodeDetailsViewModel: EpisodeDetailsViewModel = remember(episodeId) {
                episodeDetailsViewModelFactory.create(episodeId)
            }
            EpisodeDetails(episodeDetailsViewModel, modifier = Modifier)

        }

    }
}

