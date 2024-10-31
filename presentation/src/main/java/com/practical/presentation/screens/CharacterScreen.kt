package com.practical.presentation.screens

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.practical.domain.CharacterModel
import com.practical.domain.ResultState
import com.practical.presentation.R
import com.practical.presentation.ui.theme.dimens
import com.practical.presentation.viewmodel.CharacterViewModel


internal object CharacterScreenValues {
    const val IMAGE_RATIO = 1f
    const val GRID_CELLS = 2
}

@Composable
fun CharacterScreen(viewModel: CharacterViewModel) {
    val charactersState by viewModel.charactersState.collectAsState()

    val characters = remember(charactersState) {
        when (charactersState) {
            is ResultState.Success -> (charactersState as ResultState.Success).data
            else -> emptyList()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.dimens.paddingSmall),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Header()

        when (charactersState) {
            is ResultState.Loading -> {
                LoadingIndicator()
            }

            is ResultState.Success -> {
                CharacterGrid(characters)
            }

            is ResultState.Error -> {
                ErrorMessage((charactersState as ResultState.Error).exception.localizedMessage ?: "Unknown error")
            }
        }
    }
}

@Composable
fun Header() {
    Row() {
        Text(
            text = stringResource(R.string.rick_morty_app),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(MaterialTheme.dimens.paddingMedium).align(Alignment.CenterVertically)

        )
    }
}

@Composable
fun LoadingIndicator() {
    CircularProgressIndicator()
}

@Composable
fun CharacterGrid(characters: List<CharacterModel>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(CharacterScreenValues.GRID_CELLS),
        contentPadding = PaddingValues(MaterialTheme.dimens.paddingMedium)
    ) {
        items(characters.size, key = { characters[it].name }) { index ->
            CharacterItem(character = characters[index])
        }
    }
}

@Composable
fun ErrorMessage(errorMessage: String) {
    Text(
        text = errorMessage,
        color = MaterialTheme.colorScheme.error
    )
}



@Composable
private fun CharacterItem(character: CharacterModel) {
    val imageUrl = remember(character.image) { character.image }

    Card(
        modifier = Modifier
            .padding(MaterialTheme.dimens.paddingMedium)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = character.name,
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(CharacterScreenValues.IMAGE_RATIO)
            )
            Text(
                text = character.name,
                color = Color.White,
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                modifier = Modifier
                    .padding(MaterialTheme.dimens.paddingMedium)
                    .align(Alignment.BottomCenter)
            )
        }
    }
}
