package com.practical.presentation

import androidx.compose.foundation.gestures.detectVerticalDragGestures
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
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.practical.domain.CharacterModel
import com.practical.presentation.viewmodel.CharacterViewModel


object CharacterScreen {
    const val PULL_TO_REFRESH_THRESHOLD = 200
    const val IMAGE_RATIO = 1f
    const val GRID_CELLS = 2
}

@Composable
fun CharacterScreen(viewModel: CharacterViewModel) {
    val characters by viewModel.characters.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val refreshData = {
        viewModel.fetchCharacters()
    }

    SwipeRefresh(isRefreshing = isRefreshing, onRefresh = refreshData) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.spacing_medium)),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.rick_morty_app),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.spacing_small))
                    .align(Alignment.CenterHorizontally)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(CharacterScreen.GRID_CELLS),
                contentPadding = PaddingValues(dimensionResource(R.dimen.spacing_medium))
            ) {
                items(characters.size) { index ->
                    CharacterItem(characters[index])
                }
            }
        }
    }
}

@Composable
fun SwipeRefresh(isRefreshing: Boolean, onRefresh: () -> Unit, content: @Composable () -> Unit) {
    var offset by remember { mutableStateOf(0f) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectVerticalDragGestures { change, dragAmount ->
                    if (dragAmount > 0 && !isRefreshing) {
                        offset += dragAmount
                        if (offset > CharacterScreen.PULL_TO_REFRESH_THRESHOLD) {
                            onRefresh()
                            offset = 0f
                        }
                    }
                }
            }
    ) {
        content()

        if (isRefreshing) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = dimensionResource(R.dimen.spacing_large))
            )
        }
    }
}

@Composable
private fun CharacterItem(character: CharacterModel) {
    Card(
        modifier = Modifier
            .padding(dimensionResource(R.dimen.spacing_medium))
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(CharacterScreen.IMAGE_RATIO)
            )
            Text(
                text = character.name,
                color = Color.White,
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.spacing_medium)) // Adjust padding as needed
                    .align(Alignment.BottomCenter) // Align text in the center
            )
        }
    }
}
