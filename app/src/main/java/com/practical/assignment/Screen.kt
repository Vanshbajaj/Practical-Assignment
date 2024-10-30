package com.practical.assignment

sealed class Screen(val route: String) {
    object Home : Screen("home")
}
