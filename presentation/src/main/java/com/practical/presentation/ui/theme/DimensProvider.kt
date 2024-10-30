package com.practical.presentation.ui.theme

import androidx.compose.runtime.compositionLocalOf

val LocalDimens = compositionLocalOf<Dimens> { error("No dimens provided") }