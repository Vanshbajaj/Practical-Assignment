package com.practical.presentation.screens


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.practical.data.network.NetworkException
import com.practical.domain.CharacterModel
import com.practical.domain.EpisodeModel
import com.practical.presentation.R
import com.practical.presentation.UiState
import com.practical.presentation.ui.theme.Purple40
import com.practical.presentation.ui.theme.dimens
import com.practical.presentation.viewmodel.CharacterDetailsViewModel


@Composable
fun CharacterScreen(
    characterId: String,
    characterViewModel: CharacterDetailsViewModel,
    modifier: Modifier = Modifier,
    onNavigateToEpisodeScreen: (String) -> Unit,
) {
    LaunchedEffect(characterId) { characterViewModel.getCharacter(characterId) }
    val charactersState by characterViewModel.characterState.collectAsStateWithLifecycle()
    CharacterScreenContent(
        charactersState,
        onNavigateToEpisodeScreen,
        modifier.padding(MaterialTheme.dimens.paddingExtraSmall)
    )
}

@Composable
private fun CharacterScreenContent(
    state: UiState<CharacterModel>,
    onNavigateToCharacterScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier.fillMaxSize() // Fill the entire screen
    ) {
        Column {
            when (state) {
                is UiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        CircularProgressIndicator(
                            Modifier.align(Alignment.Center)
                        )
                    }
                }

                is UiState.Success -> {
                    // When data is loaded, display the character details
                    TopData(character = state.data, onNavigateToCharacterScreen)
                }

                is UiState.Error -> {
                    // Error handling for network failure, etc.
                    when (state.exception) {
                        is NetworkException.ClientNetworkException -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                Text(
                                    text = stringResource(R.string.no_internet_data),
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodyLarge, // Optional: Add style
                                    modifier = Modifier.align(Alignment.Center) // Ensure it is centered
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TopData(
    character: CharacterModel,
    onNavigateToCharacterScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Column(modifier = modifier.padding(horizontal = MaterialTheme.dimens.paddingExtraSmall)) {
        CharacterImage(character = character, screenHeight = screenHeight)
        CharacterInfo(character = character)
        CharacterEpisodes(character = character, onNavigateToCharacterScreen)
    }
}

@Composable
private fun CharacterImage(character: CharacterModel, screenHeight: Dp) {
    AsyncImage(
        model = character.image,
        contentDescription = character.name,
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeight / CharacterScreenValues.SCREEN_HEIGHT_BY_TWO)
    )
}

@Composable
private fun CharacterInfo(character: CharacterModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        CharacterRow(
            label = stringResource(R.string.status),
            value = character.status,
            color = if (character.status == "Alive") Purple40 else Color.Red
        )
        CharacterRow(
            label = stringResource(R.string.species),
            value = character.species,
            color = Purple40
        )
        CharacterRow(label = stringResource(R.string.gender), value = character.status)
        Text(
            text = stringResource(R.string.episodes),
            fontSize = MaterialTheme.typography.labelLarge.fontSize,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(
                horizontal = MaterialTheme.dimens.paddingMedium,
                vertical = MaterialTheme.dimens.paddingSmall
            )
        )
    }
}


@Composable
private fun CharacterRow(label: String, value: String, color: Color = Purple40) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            modifier = Modifier.padding(horizontal = MaterialTheme.dimens.paddingExtraSmall),
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = color
        )
    }
}


@Composable
private fun CharacterEpisodes(
    character: CharacterModel,
    onNavigateToCharacterScreen: (String) -> Unit,
) {
    LazyRow(modifier = Modifier.padding(MaterialTheme.dimens.paddingSmall)) {
        items(character.episodes.size) { episode ->
            EpisodeCard(episode = character.episodes[episode], onNavigateToCharacterScreen)
        }
    }
}

@Composable
private fun EpisodeCard(
    episode: EpisodeModel,
    onNavigateToCharacterScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .padding(horizontal = MaterialTheme.dimens.paddingExtraSmall)
            .width(MaterialTheme.dimens.cardWidth)
            .clickable(
                enabled = episode.id
                    .isEmpty()
                    .not()
            ) {
                episode.id.let { onNavigateToCharacterScreen.invoke(it) }

            }
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
    const val SCREEN_HEIGHT_BY_TWO = 2
}

