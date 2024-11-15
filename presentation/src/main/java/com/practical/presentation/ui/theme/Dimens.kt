package com.practical.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimens(
    val paddingExtraSmall: Dp,
    val paddingSmall: Dp,
    val paddingMedium: Dp,
    val paddingLarge: Dp,
    val marginSmall: Dp,
    val marginMedium: Dp,
    val marginLarge: Dp,
    val cardWidth: Dp,
)


val MaterialDimens = Dimens(
    paddingExtraSmall = 4.dp,
    paddingSmall = 8.dp,
    paddingMedium = 16.dp,
    paddingLarge = 24.dp,
    marginSmall = 4.dp,
    marginMedium = 12.dp,
    marginLarge = 20.dp,
    cardWidth = 120.dp
)
val LocalDimens = compositionLocalOf<Dimens> { error("No dimens provided") }
val MaterialTheme.dimens: Dimens
    @Composable
    get() = LocalDimens.current
