package com.practical.data

import com.data.graphql.CharacterDetailsQuery
import com.practical.domain.CharacterModel
import com.practical.domain.EpisodeModel
import com.practical.domain.OriginModel


fun CharacterDetailsQuery.Character.toCharacterModel(): CharacterModel {
    return CharacterModel(
        name = this.name,
        status = this.status,
        species = this.species,
        gender = this.gender,
        origin = this.origin.toOriginModel(),
        image = this.image,
        episodes = this.episode.mapNotNull { it?.toEpisodeModel() }
    )
}

fun CharacterDetailsQuery.Origin.toOriginModel(): OriginModel {
    return OriginModel(
        name = this.name,
        dimension = this.dimension
    )

}

fun CharacterDetailsQuery.Episode.toEpisodeModel(): EpisodeModel {
    return EpisodeModel(
        id = this.id,
        name = this.name
    )
}
