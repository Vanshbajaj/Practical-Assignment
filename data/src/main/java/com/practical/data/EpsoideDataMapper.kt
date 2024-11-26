package com.practical.data
import com.data.graphql.EpisodeQuery
import com.practical.domain.Character
import com.practical.domain.Episode
import com.practical.domain.EpisodeData
import com.practical.domain.EpisodeModelDetails


fun EpisodeQuery.Episode.toEpisodeModelDetails(): EpisodeModelDetails {
    return EpisodeModelDetails(
        data = EpisodeData(
            episode = Episode(
                id = this.id,
                name = this.name,
                airDate = this.air_date,
                episode = this.episode,
                created = this.created,
                characters = this.characters.mapNotNull { it?.toCharacter() }
            )
        )
    )
}

fun EpisodeQuery.Character.toCharacter(): Character {
    return Character(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        type = this.type,
        gender = this.gender,
        image = this.image,
        created = this.created
    )
}

