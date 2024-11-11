package com.practical.data

import com.data.graphql.CharacterDetailsQuery
import com.practical.domain.CharacterModel
import com.practical.domain.EpisodeModel
import com.practical.domain.OriginModel


fun CharacterDetailsQuery.Character.toCharacterModel(): CharacterModel {
    if (!this.id.isNullOrEmpty()) {
        return CharacterModel(
            id = this.id,
            name = this.name.orEmpty(),
            status = this.status.orEmpty(),
            species = this.species.orEmpty(),
            type = this.type.orEmpty(),
            origin = this.origin?.toOriginModel() ?: OriginModel(),
            image = this.image.orEmpty(),
            created = this.created.orEmpty(),
            episodes = this.episode.mapNotNull { it?.toEpisodeModel() }
        )
    }
    return CharacterModel()

}

fun CharacterDetailsQuery.Origin.toOriginModel(): OriginModel {
    if (!id.isNullOrEmpty()) {
        return OriginModel(
            id = this.id,
            name = this.name.orEmpty(),
            type = this.type.orEmpty(),
            dimension = this.dimension.orEmpty(),
            created = this.created.orEmpty()
        )
    }
    return OriginModel()
}

fun CharacterDetailsQuery.Episode.toEpisodeModel(): EpisodeModel {
    if (!this.id.isNullOrEmpty()) {
        return EpisodeModel(
            id = this.id,
            name = this.name.orEmpty(),
            airDate = this.air_date.orEmpty(),
            episode = this.episode.orEmpty(),
            created = this.created.orEmpty()
        )
    }
    return EpisodeModel()
}
