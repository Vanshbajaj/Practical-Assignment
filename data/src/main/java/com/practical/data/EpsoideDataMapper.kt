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
                name = this.name,
                airDate = this.air_date,
                characters = this.characters.mapNotNull { it?.toCharacter() }
            )
        )
    )
}

fun EpisodeQuery.Character.toCharacter(): Character {
    return Character(
        id = this.id,
        image = this.image
    )
}

