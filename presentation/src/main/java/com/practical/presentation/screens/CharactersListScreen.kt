package com.practical.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.practical.data.network.NetworkException
import com.practical.domain.CharactersListModel
import com.practical.presentation.R
import com.practical.presentation.UiState
import com.practical.presentation.common.ErrorMessage
import com.practical.presentation.ui.theme.dimens
import com.practical.presentation.viewmodel.CharacterViewModel

@Composable
fun CharacterListScreen(
    viewModel: CharacterViewModel,
    modifier: Modifier = Modifier,
    onNavigateToCharacterScreen: (String) -> Unit,
) {
    val charactersState by viewModel.charactersState.collectAsStateWithLifecycle()
    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.rick_morty_app),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(MaterialTheme.dimens.paddingSmall)
            )
        }
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (val state = charactersState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
                }

                is UiState.Success -> {
                    CharacterGrid(state.data, onNavigateToCharacterScreen)
                }

                is UiState.Error -> {
                    when (state.exception) {
                        is NetworkException.ClientNetworkException -> {
                            Text(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                text = stringResource(R.string.no_internet_data),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                }
            }
        }
    }
}

@Composable
private fun CharacterGrid(
    characters: List<CharactersListModel>,
    onNavigateToCharacterScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(CharactersListScreen.GRID_CELLS),
        contentPadding = PaddingValues(MaterialTheme.dimens.paddingSmall)
    ) {
        items(characters.size) { index ->
            CharacterItem(character = characters[index], onNavigateToCharacterScreen)
        }
    }
}


@Composable
private fun CharacterItem(
    character: CharactersListModel,
    onNavigateToCharacterScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val imageUrl = remember(character.image) { character.image }

    Card(modifier = modifier
        .padding(MaterialTheme.dimens.paddingSmall)
        .fillMaxWidth()
        .clickable(
            enabled = character.id
                .isNullOrEmpty()
                .not()
        ) {
            character.id?.let { characterId -> onNavigateToCharacterScreen.invoke(characterId) }
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = character.name,
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(CharactersListScreen.IMAGE_RATIO)
            )
            character.name?.let {
                Text(
                    text = it,
                    color = Color.White,
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    modifier = Modifier
                        .padding(MaterialTheme.dimens.paddingSmall)
                        .align(Alignment.BottomCenter)
                )
            }
        }
    }
}

internal object CharactersListScreen {
    const val IMAGE_RATIO = 1f
    const val GRID_CELLS = 2
}
