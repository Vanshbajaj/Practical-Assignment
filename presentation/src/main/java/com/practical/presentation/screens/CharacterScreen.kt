package com.practical.presentation.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.practical.domain.CharacterModel
import com.practical.domain.ResultState
import com.practical.presentation.R
import com.practical.presentation.getDynamicGridColumns
import com.practical.presentation.ui.theme.dimens
import com.practical.presentation.viewmodel.CharacterViewModel


@Composable
fun CharacterScreen(viewModel: CharacterViewModel, modifier: Modifier = Modifier) {
    // Collect the state from the viewModel
    val charactersState by viewModel.charactersState.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current.density

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

        // Handle loading, success, and error states
        when (val state = charactersState) {
            is ResultState.Loading -> {
                CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
            }
            is ResultState.Success -> {
                // Determine the number of columns dynamically based on screen size and density
                CharacterGrid(
                    characters = state.data,
                    configuration = configuration,
                    density = density
                )
            }
            is ResultState.Error -> {
                state.exception.localizedMessage?.let {
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
private fun CharacterGrid(characters: List<CharacterModel>, configuration: Configuration, density: Float) {
    val screenWidthDp = configuration.screenWidthDp.dp
    val gridColumns = getDynamicGridColumns(screenWidthDp, configuration, density)

    LazyVerticalGrid(
        columns = GridCells.Fixed(gridColumns),
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
        CharacterScreenValues.IMAGE_RATIO_LANDSCAPE
    } else {
        CharacterScreenValues.IMAGE_RATIO
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
    const val IMAGE_RATIO_LANDSCAPE = 1.5f
}