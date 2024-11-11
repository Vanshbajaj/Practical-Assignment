package com.practical.domain


data class CharacterModel(
    val id: String = "",
    val name: String = "",
    val status: String = "",
    val species: String = "",
    val type: String = "",
    val origin: OriginModel = OriginModel(),
    val image: String = "",
    val created: String = "",
    val episodes: List<EpisodeModel> = emptyList(),
)

data class OriginModel(
    val id: String = "",
    val name: String = "",
    val type: String = "",
    val dimension: String = "",
    val created: String = "",
)

data class EpisodeModel(
    val id: String = "",
    val name: String = "",
    val airDate: String = "",
    val episode: String = "",
    val created: String = "",
)




