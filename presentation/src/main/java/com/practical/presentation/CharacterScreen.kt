package com.practical.presentation

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.practical.domain.CharacterModel
import com.practical.presentation.viewmodel.CharacterViewModel


@Composable
fun characterScreen(viewModel: CharacterViewModel) {
    val characters by viewModel.characters.collectAsState()
    val gridCells = 2
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.paddingSmall),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.rick_morty_app),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(Dimens.extraPaddingSmall)
                .align(Alignment.CenterHorizontally)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(gridCells),
            contentPadding = PaddingValues(Dimens.paddingSmall)
        ) {
            items(characters.size) { character ->
                characterItem(characters[character])
            }
        }
    }
}


@Composable
private fun characterItem(character: CharacterModel) {
    Card(
        modifier = Modifier
            .padding(Dimens.paddingSmall)
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
                    .aspectRatio(Ratio.oneFloat)
            )
            Text(
                text = character.name,
                color = Color.White,
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                modifier = Modifier
                    .padding(Dimens.paddingSmall) // Adjust padding as needed
                    .align(Alignment.BottomCenter) // Align text in the center
            )
        }
    }
}

