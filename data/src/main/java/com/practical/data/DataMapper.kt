package com.practical.data

import com.data.graphql.CharacterDetailsQuery
import com.practical.domain.CharacterModel
import com.practical.domain.EpisodeModel
import com.practical.domain.OriginModel

fun CharacterDetailsQuery.Character.toCharacterModel(): CharacterModel {
    return CharacterModel(
        id = this.id.orEmpty(),
        name = this.name.orEmpty(),
        status = this.status.orEmpty(),
        species = this.species.orEmpty(),
        type = this.type.orEmpty(),
        origin = this.origin?.toOriginModel() ?: OriginModel("", "", "", "", ""),
        image = this.image.orEmpty(),
        created = this.created.orEmpty(),
        episodes = this.episode.map { it!!.toEpisodeModel() }
    )
}

fun CharacterDetailsQuery.Origin.toOriginModel(): OriginModel {
    return OriginModel(
        id = this.id.orEmpty(),
        name = this.name.orEmpty(),
        type = this.type.orEmpty(),
        dimension = this.dimension.orEmpty(),
        created = this.created.orEmpty()
    )
}

fun CharacterDetailsQuery.Episode.toEpisodeModel(): EpisodeModel {
    return EpisodeModel(
        id = this.id.orEmpty(),
        name = this.name.orEmpty(),
        airDate = this.air_date.orEmpty(),
        episode = this.episode.orEmpty(),
        created = this.created.orEmpty()
    )
}
