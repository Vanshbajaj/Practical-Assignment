package com.practical.data

import com.data.graphql.CharacterDetailsQuery
import com.practical.domain.CharacterModel
import com.practical.domain.EpisodeModel
import com.practical.domain.OriginModel


fun CharacterDetailsQuery.Character.toCharacterModel(): CharacterModel {
    if (this.id.isNotEmpty()) {
        return CharacterModel(
            id = this.id,
            name = this.name,
            status = this.status,
            species = this.species,
            type = this.type,
            origin = this.origin.toOriginModel(),
            image = this.image,
            created = this.created,
            episodes = this.episode.mapNotNull { it?.toEpisodeModel() }
        )
    }
    return CharacterModel()

}

fun CharacterDetailsQuery.Origin.toOriginModel(): OriginModel {
    if (id.isNotEmpty()) {
        return OriginModel(
            id = this.id,
            name = this.name,
            type = this.type,
            dimension = this.dimension,
            created = this.created
        )
    }
    return OriginModel()
}

fun CharacterDetailsQuery.Episode.toEpisodeModel(): EpisodeModel {
    if (this.id.isNotEmpty()) {
        return EpisodeModel(
            id = this.id,
            name = this.name,
            airDate = this.air_date,
            episode = this.episode,
            created = this.created
        )
    }
    return EpisodeModel()
}
