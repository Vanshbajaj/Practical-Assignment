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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
<<<<<<<< HEAD:presentation/src/main/java/com/practical/presentation/screens/CharacterScreen.kt
import androidx.compose.ui.unit.dp
========
import androidx.compose.ui.res.stringResource
>>>>>>>> cf9b900 (Added Test Case For Repository):presentation/src/main/java/com/practical/presentation/screens/CharactersListScreen.kt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.practical.domain.CharacterModel
import com.practical.domain.ResultState
<<<<<<<< HEAD:presentation/src/main/java/com/practical/presentation/screens/CharacterScreen.kt
import com.practical.presentation.getDynamicGridColumns
========
import com.practical.presentation.R
>>>>>>>> cf9b900 (Added Test Case For Repository):presentation/src/main/java/com/practical/presentation/screens/CharactersListScreen.kt
import com.practical.presentation.ui.theme.dimens
import com.practical.presentation.viewmodel.CharacterViewModel

@Composable
    val charactersState by viewModel.charactersState.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.dimens.paddingSmall),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(MaterialTheme.dimens.paddingSmall)
                .align(Alignment.CenterHorizontally)

        )

            is ResultState.Loading -> {
                CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
            }

            is ResultState.Success -> {
                if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    CharacterGrid(characters = state.data, isLandscape = true)
                } else {
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
    LazyVerticalGrid(
        columns = gridCells,
        contentPadding = PaddingValues(MaterialTheme.dimens.paddingSmall)
    ) {
            CharacterItem(character = characters[index])
        }
    }
}

<<<<<<<< HEAD:presentation/src/main/java/com/practical/presentation/screens/CharacterScreen.kt
========



@Composable
private fun CharacterItem(character: CharacterModel) {
    val imageUrl = remember(character.image) { character.image }
    } else {
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
    const val GRID_CELLS = 2
    const val GRID_CELLS_LANDSCAPE = 3
}
>>>>>>>> cf9b900 (Added Test Case For Repository):presentation/src/main/java/com/practical/presentation/screens/CharactersListScreen.kt
