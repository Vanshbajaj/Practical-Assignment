package com.practical.presentation.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.practical.data.network.NetworkException
import com.practical.presentation.R

@Composable
fun ErrorText(message: Int, modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize()) {
        Text(
            text = stringResource(id = message),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.Center)
        )
    }

}

@Composable
fun ErrorMessage(exception: Throwable, modifier: Modifier = Modifier) {
    when (exception) {
        is NetworkException.ClientNetworkException -> {
            ErrorText(R.string.no_internet_data,modifier)
        }

        is NetworkException.ApolloClientException -> {
            ErrorText(R.string.graphql_error)
        }

    }
}
