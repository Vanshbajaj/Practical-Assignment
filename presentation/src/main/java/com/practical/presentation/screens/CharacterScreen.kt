package com.practical.presentation.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.practical.domain.CharacterModel
import com.practical.domain.EpisodeModel
import com.practical.domain.ResultState
import com.practical.presentation.R
import com.practical.presentation.ui.theme.dimens
import com.practical.presentation.viewmodel.CharacterDetailsViewModel


@Composable
fun CharacterScreen(
    characterId: String,
    characterViewModel: CharacterDetailsViewModel,
) {
    LaunchedEffect(characterId) { characterViewModel.getCharacter(characterId) }
    val charactersState by characterViewModel.characterState.collectAsStateWithLifecycle()
    CharacterScreenContent(charactersState)
}


@Composable
private fun CharacterScreenContent(
    state: ResultState<CharacterModel>,
) {
    when (state) {
        is ResultState.Loading -> {
            Row(horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.dimens.paddingMedium)
            ) {
                CircularProgressIndicator(
                    Modifier.align(
                        Alignment.CenterVertically
                    )
                )
            }
        }

        is ResultState.Success -> {
            // When data is loaded, display the character details
            TopData(character = state.data)
        }

        is ResultState.Error -> {
            // Show an error message
            Text(text = "Failed to load character")
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
            fontWeight = FontWeight.Bold
        )

        Text(text = stringResource(R.string.status, character.status))
        Text(text = stringResource(R.string.species, character.species))
        Text(text = stringResource(R.string.gender))
        Text(
            text = stringResource(R.string.episodes),
            fontSize = MaterialTheme.typography.labelLarge.fontSize,
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
