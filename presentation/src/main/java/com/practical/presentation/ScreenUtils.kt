package com.practical.presentation

import android.content.res.Configuration
import androidx.compose.ui.unit.Dp



fun getDynamicGridColumns(screenWidthDp: Dp, configuration: Configuration, density: Float): Int {
    // Convert Dp to pixels using the density value
    val screenWidthPx = (screenWidthDp.value * density).toInt()

    // Device is in landscape
    return when {
        screenWidthPx < 600 -> 2 // Small devices (phones, narrow screens)
        screenWidthPx in 600..1200 -> 3 // Tablets, medium size devices
        screenWidthPx > 1200 -> 4 // Large screens or foldables in extended mode (large tablets, desktops)
        else -> 2
    }
}