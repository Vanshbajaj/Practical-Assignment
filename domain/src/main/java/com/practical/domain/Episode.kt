package com.practical.domain

data class Episode(
    val airDate: String="",
    val characters: List<Character> = emptyList(),
    val name: String="",
)
