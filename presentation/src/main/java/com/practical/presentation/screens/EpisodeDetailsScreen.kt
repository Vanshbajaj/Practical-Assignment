package com.practical.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.practical.data.network.NetworkException
import com.practical.domain.Character
import com.practical.domain.Episode
import com.practical.presentation.R
import com.practical.presentation.UiState
import com.practical.presentation.common.ErrorMessage
import com.practical.presentation.ui.theme.Purple
import com.practical.presentation.ui.theme.dimens

@Composable
fun EpisodeDetails(uiState: State<UiState<Episode>>, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(MaterialTheme.dimens.paddingExtraSmall)
            .fillMaxSize()
    ) {
        val episodeState by uiState
        when (val state = episodeState) {
            UiState.Loading -> {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }

            is UiState.Error -> {
                when (state.exception) {
                    is NetworkException.ClientNetworkException -> {
                        ErrorMessage(exception = state.exception, modifier = Modifier.fillMaxSize())
                    }

                }
            }

            is UiState.Success -> {
                PilotScreen(state.data)

            }
        }
    }
}

@Composable
private fun LabeledText(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = label)
        Text(text = value, fontSize = MaterialTheme.typography.titleMedium.fontSize, color = Purple)
    }
}

@Composable
private fun PilotScreen(data: Episode, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(MaterialTheme.dimens.paddingMedium)) {
        LabeledText(
            label = stringResource(R.string.label_episode_name),
            value = data.name
        )

        LabeledText(
            label = stringResource(R.string.label_air_date),
            value = data.airDate
        )

        Text(text = stringResource(R.string.label_characters))

        LazyVerticalGrid(
            columns = GridCells.Fixed(EpisodeDetailsScreenValues.GRID_CELLS),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.paddingSmall),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.paddingSmall)
        ) {
            items(data.characters.size) { index ->
                CharacterItem(data.characters[index])
            }
        }
    }
}

@Composable
private fun CharacterItem(character: Character, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.padding(MaterialTheme.dimens.paddingExtraSmall),
        shape = RoundedCornerShape(MaterialTheme.dimens.paddingSmall)
    ) {
        AsyncImage(
            model = character.image,
            contentDescription = character.id,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(EpisodeDetailsScreenValues.IMAGE_RATIO), // Maintain aspect ratio
        )

    }
}

internal object EpisodeDetailsScreenValues {
    const val IMAGE_RATIO = 1f
    const val GRID_CELLS = 2
}
