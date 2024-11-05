package com.practical.presentation.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.practical.domain.CharacterModel
import com.practical.domain.ResultState
import com.practical.presentation.R
import com.practical.presentation.ui.theme.dimens
import com.practical.presentation.viewmodel.CharacterViewModel

@Composable
fun CharacterScreen(viewModel: CharacterViewModel, modifier: Modifier = Modifier) {
    val charactersState by viewModel.charactersState.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.dimens.paddingSmall),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.rick_morty_app),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(MaterialTheme.dimens.paddingSmall)
                .align(Alignment.CenterHorizontally)

        )

        when (val state = charactersState) {
            is ResultState.Loading -> {
                CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
            }

            is ResultState.Success -> {
                if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    // Adjust layout for landscape
                    CharacterGrid(characters = state.data, isLandscape = true)
                } else {
                    // Portrait layout
                    CharacterGrid(characters = state.data, isLandscape = false)
                }
            }

            is ResultState.Error -> {
                (state).exception.localizedMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}


@Composable
private fun CharacterGrid(characters: List<CharacterModel>, isLandscape: Boolean) {
    val gridCells = if (isLandscape) {
        GridCells.Fixed(3) // More columns in landscape
    } else {
        GridCells.Fixed(2) // Fewer columns in portrait
    }

    LazyVerticalGrid(
        columns = gridCells,
        contentPadding = PaddingValues(MaterialTheme.dimens.paddingSmall)
    ) {
        items(characters.size, key = { characters[it].name }) { index ->
            CharacterItem(character = characters[index])
        }
    }
}

@Composable
private fun CharacterItem(character: CharacterModel) {
    val imageUrl = remember(character.image) { character.image }
    val aspectRatio = if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        1.5f // Wider aspect ratio in landscape
    } else {
        1f // Square in portrait
    }

    Card(
        modifier = Modifier
            .padding(MaterialTheme.dimens.paddingSmall)
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
                    .aspectRatio(aspectRatio)
            )
            Text(
                text = character.name,
                color = Color.White,
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                modifier = Modifier
                    .padding(MaterialTheme.dimens.paddingSmall)
                    .align(Alignment.BottomCenter)
            )
        }
    }
}


internal object CharacterScreenValues {
    const val IMAGE_RATIO = 1f
    const val GRID_CELLS = 2
}
