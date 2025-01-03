package com.practical.assignment

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
data class CharacterScreenData(val id: String)

@Serializable
data class EpisodeScreenData(val id: String)
