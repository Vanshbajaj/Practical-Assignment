package com.practical.domain

data class EpisodeModelDetails(
    val data: EpisodeData,
)

data class EpisodeData(
    val episode: Episode,
)

data class Episode(
    val airDate: String,
    val characters: List<Character>,
    val created: String,
    val episode: String,
    val id: String,
    val name: String,
)

data class Character(
    val created: String,
    val gender: String,
    val id: String,
    val image: String,
    val name: String,
    val species: String,
    val status: String,
    val type: String,
)
