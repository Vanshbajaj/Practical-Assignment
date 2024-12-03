package com.practical.domain


data class CharacterModel(
    val name: String = "",
    val status: String = "",
    val species: String = "",
    val gender: String = "",
    val origin: OriginModel = OriginModel(),
    val image: String = "",
    val episodes: List<EpisodeModel> = emptyList(),
)

data class OriginModel(
    val name: String = "",
    val dimension: String? = null,
)

data class EpisodeModel(
    val id: String = "",
    val name: String = "",
)




