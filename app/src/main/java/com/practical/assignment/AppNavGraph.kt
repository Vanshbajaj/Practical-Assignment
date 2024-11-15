package com.practical.assignment

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.practical.presentation.screens.CharacterListScreen
import com.practical.presentation.screens.CharacterScreen
import com.practical.presentation.viewmodel.CharacterDetailsViewModel
import com.practical.presentation.viewmodel.CharacterViewModel
import com.practical.presentation.viewmodel.ViewModelFactory

@Composable
fun AppNavGraph(navController: NavHostController, viewModelFactory: ViewModelFactory) {
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
            CharacterScreen(screen.id, characterViewModel)
        }
    }
}


//@Composable
//fun RickAndMortyAppBar(title: String) {
//    val topBarTitle = remember { mutableStateOf("text") }
//    Row(
//        modifier =
//        Modifier
//            .fillMaxWidth()
//            .height(MaterialTheme.dimens.paddingMedium)
//            .background(MaterialTheme.colorScheme.surface),
//        horizontalArrangement = Arrangement.Center,
//        verticalAlignment = Alignment.CenterVertically,
//    ) {
//        Text(title, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
//    }
//}



