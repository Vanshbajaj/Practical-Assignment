package com.practical.assignment

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
data class CharacterScreenData(val id: String, val name: String)
