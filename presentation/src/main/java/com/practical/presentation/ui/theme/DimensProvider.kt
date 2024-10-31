package com.practical.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class DimensProvider(
    val paddingSmall: Dp,
    val paddingMedium: Dp,
    val paddingLarge: Dp,
    val marginSmall: Dp,
    val marginMedium: Dp,
    val marginLarge: Dp,
)


val MaterialDimens = DimensProvider(
    paddingSmall = 8.dp,
    paddingMedium = 16.dp,
    paddingLarge = 24.dp,
    marginSmall = 4.dp,
    marginMedium = 12.dp,
    marginLarge = 20.dp
)

val MaterialTheme.dimens: DimensProvider
    get() = MaterialDimens
