package com.practical.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.practical.data.network.NetworkException
import com.practical.presentation.UiState
import com.practical.presentation.ui.theme.dimens
import com.practical.presentation.viewmodel.EpisodeDetailsViewModel

@Composable
fun EpisodeDetails(viewModel: EpisodeDetailsViewModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(MaterialTheme.dimens.paddingExtraSmall)
    ) {
        val episodeState by viewModel.episodeState.collectAsState()
        when (val state = episodeState) {
            UiState.Loading -> {
                CircularProgressIndicator()
            }

            is UiState.Error -> {
                when(state.exception){
                    is NetworkException.ClientNetworkException->{

                    }

                }
            }

            is UiState.Success -> {

            }
        }
    }
}
