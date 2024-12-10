package com.practical.presentation.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.practical.domain.CharacterModel
import com.practical.domain.EpisodeModel
import com.practical.presentation.R
import com.practical.presentation.UiState
import com.practical.presentation.common.ErrorMessage
import com.practical.presentation.ui.theme.Purple
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
        modifier.fillMaxSize()
    ) {
        when (state) {
            is UiState.Loading -> {
                CircularProgressIndicator(
                    Modifier.align(Alignment.Center)
                )
            }

            is UiState.Success -> {
                // When data is loaded, display the character details
                TopData(character = state.data, onNavigateToCharacterScreen)
            }

            is UiState.Error -> {
                // Error handling for network failure, etc.
                ErrorMessage(exception = state.exception, modifier = Modifier.fillMaxSize())
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
<<<<<<< HEAD
        AsyncImage(
            model = character.image,
            contentDescription = character.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight / CharacterScreenValues.SCREEN_HEIGHT_BY_TWO)
=======
        CharacterImage(character = character, screenHeight = screenHeight)
        Text(character.name, fontSize = MaterialTheme.typography.headlineLarge.fontSize)
        CharacterInfo(character = character)
        CharacterEpisodes(character = character, onNavigateToCharacterScreen)
    }
}

@Composable
private fun CharacterImage(
    character: CharacterModel,
    screenHeight: Dp,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = character.image,
        contentDescription = character.name,
        modifier = modifier
            .fillMaxWidth()
            .height(screenHeight / CharacterScreenValues.SCREEN_HEIGHT_BY_TWO)
    )

}

@Composable
private fun CharacterInfo(character: CharacterModel, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        CharacterRow(
            label = stringResource(R.string.status),
            value = character.status,
            color = if (character.status == "Alive") Purple40 else Color.Red
>>>>>>> 24aae28 (Added Modifier to CharacterScreen)
        )

        Text(
            text = character.name,
            fontSize = MaterialTheme.typography.headlineLarge.fontSize
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = stringResource(R.string.status),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.paddingExtraSmall),
                text = character.status,
                style = MaterialTheme.typography.titleMedium,
                color = if (character.status == stringResource(R.string.label_alive)) Purple40 else Color.Red
            )
        }

        // Species Row
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = stringResource(R.string.species),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.paddingExtraSmall),
                text = character.species,
                style = MaterialTheme.typography.titleMedium,
                color = Purple40
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = stringResource(R.string.gender),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.paddingExtraSmall),
                text = character.gender,
                style = MaterialTheme.typography.titleMedium, color = Purple40
        )
        if (character.gender == stringResource(R.string.txt_male)) {
                Image(painter = painterResource(R.drawable.ic_male), contentDescription = "")
            } else {
                Image(painter = painterResource(R.drawable.ic_female), contentDescription = "")
            }
        }
        Spacer(Modifier.padding(MaterialTheme.dimens.paddingExtraSmall))
        Text(
            text = stringResource(R.string.label_origin),
            fontSize = MaterialTheme.typography.titleLarge.fontSize
        )
        Spacer(Modifier.padding(MaterialTheme.dimens.paddingExtraSmall))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = stringResource(R.string.label_name),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.paddingExtraSmall),
                text = character.origin.name,
                style = MaterialTheme.typography.titleMedium,
                color = Purple40
            )
        }
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = stringResource(R.string.label_dimension), style = MaterialTheme.typography.titleMedium
        )
        Text(
            modifier = Modifier.padding(horizontal = MaterialTheme.dimens.paddingExtraSmall),
            text = character.origin.dimension.orEmpty(),
                style = MaterialTheme.typography.titleMedium,
                color = Purple40
            )
        }
        Text(
            text = stringResource(R.string.episodes),
            fontSize = MaterialTheme.typography.labelLarge.fontSize,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(
                horizontal = MaterialTheme.dimens.paddingMedium,
                vertical = MaterialTheme.dimens.paddingSmall
            )
        )
<<<<<<< HEAD
        LazyRow(modifier = Modifier.padding(MaterialTheme.dimens.paddingSmall)) {
            items(character.episodes.size) { episode ->
                EpisodeCard(episode = character.episodes[episode], onNavigateToCharacterScreen)
            }
=======
    }
}


@Composable
private fun CharacterRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    color: Color = Purple40,
) {
    Row(
        modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = label, style = MaterialTheme.typography.titleMedium
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
    modifier: Modifier = Modifier,
) {
    LazyRow(modifier = modifier.padding(MaterialTheme.dimens.paddingSmall)) {
        items(character.episodes.size) { episode ->
            EpisodeCard(episode = character.episodes[episode], onNavigateToCharacterScreen)
>>>>>>> 24aae28 (Added Modifier to CharacterScreen)
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
            .height(MaterialTheme.dimens.cardWidth)
            .clickable(
                enabled = episode.id.isNotEmpty()
            ) {
                episode.id.let { onNavigateToCharacterScreen.invoke(it) }
            },
        shape = RoundedCornerShape(MaterialTheme.dimens.paddingMedium),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Purple)
        ) {
            Column(
                verticalArrangement = Arrangement.Center, // Center content vertically
                horizontalAlignment = Alignment.CenterHorizontally, // Center content horizontally
                modifier = Modifier.fillMaxSize() // Make Column take up the full size of the Box
            ) {
                Text(
                    text = episode.name,
                    style = MaterialTheme.typography.bodyLarge, // You can customize this style
                    color = Color.White, // Set text color to white
                    modifier =
                    Modifier.padding(MaterialTheme.dimens.paddingSmall) // Optional padding for better spacing
                )
            }
        }
    }
}

internal object CharacterScreenValues {
    const val SCREEN_HEIGHT_BY_TWO = 2
}

