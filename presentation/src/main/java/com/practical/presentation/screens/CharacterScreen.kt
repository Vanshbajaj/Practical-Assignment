package com.practical.presentation.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.practical.domain.CharacterModel
import com.practical.domain.EpisodeModel
import com.practical.domain.ResultState
import com.practical.presentation.R
import com.practical.presentation.ui.theme.dimens
import com.practical.presentation.viewmodel.CharacterDetailsViewModel
import com.practical.presentation.viewmodel.CharacterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterScreen(
    characterId: String,
    characterName: String,
    characterViewModel: CharacterDetailsViewModel,
    navController: NavController,
) {
    val getCharacter by rememberSaveable(characterId) { mutableStateOf(characterId) }
    characterViewModel.getCharacter(getCharacter)

    val charactersState by characterViewModel.characterState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = characterName)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,  // Back button icon
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        CharacterScreenContent(charactersState, Modifier.padding(paddingValues))

    }
}

@Composable
private fun CharacterScreenContent(
    state: ResultState<CharacterModel>,
    modifier: Modifier = Modifier,
) {
    when (state) {
        is ResultState.Loading -> {
            // Show a loading indicator
            CircularProgressIndicator()
        }

        is ResultState.Success -> {
            // When data is loaded, display the character details
            TopData(character = state.data)
        }

        is ResultState.Error -> {
            // Show an error message
            Text(text = "Failed to load character", modifier = modifier)
        }
    }
}


@Composable
private fun TopData(character: CharacterModel) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    Column(modifier = Modifier.padding(horizontal = MaterialTheme.dimens.paddingSmall)) {

        AsyncImage(
            model = character.image,
            contentDescription = character.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight / CharacterScreenValues.screenHeightbyTwo)
        )
        Text(
            text = character.name,
            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
            fontWeight = FontWeight.Bold,
        )

        Text(
            text = stringResource(R.string.status, character.status),

            )
        Text(
            text = stringResource(R.string.species, character.species),

            )
        Text(
            text = stringResource(R.string.gender),
        )


        Text(
            text = stringResource(R.string.episodes),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(
                horizontal = MaterialTheme.dimens.paddingMedium,
                vertical = MaterialTheme.dimens.paddingSmall
            )
        )

        LazyRow(
            modifier = Modifier.padding(MaterialTheme.dimens.paddingSmall)
        ) {
            items(character.episodes.size) { episode ->
                EpisodeCard(episode = character.episodes[episode])
            }
        }
    }
}

@Composable
private fun EpisodeCard(episode: EpisodeModel) {
    Card(
        modifier = Modifier
            .padding(horizontal = MaterialTheme.dimens.paddingExtraSmall)
            .width(MaterialTheme.dimens.cardWidth)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(MaterialTheme.dimens.paddingMedium)

        ) {
            Text(text = episode.name, fontSize = MaterialTheme.typography.labelLarge.fontSize)
        }
    }
}

internal object CharacterScreenValues {
    const val screenHeightbyTwo = 2
}
