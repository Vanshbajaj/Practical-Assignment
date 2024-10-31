package com.practical.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimens(
    val paddingSmall: Dp,
    val paddingMedium: Dp,
    val paddingLarge: Dp,
    val marginSmall: Dp,
    val marginMedium: Dp,
    val marginLarge: Dp,
)


val MaterialDimens = Dimens(
    paddingSmall = 8.dp,
    paddingMedium = 16.dp,
    paddingLarge = 24.dp,
    marginSmall = 4.dp,
    marginMedium = 12.dp,
    marginLarge = 20.dp
)
val LocalDimens = compositionLocalOf<Dimens> { error("No dimens provided") }
val MaterialTheme.dimens: Dimens
    @Composable
    get() = LocalDimens.current
