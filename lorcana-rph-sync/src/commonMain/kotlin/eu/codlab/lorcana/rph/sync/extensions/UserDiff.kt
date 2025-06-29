package eu.codlab.lorcana.rph.sync.extensions

import eu.codlab.lorcana.rph.rounds.matches.EventMatchPlayer
import eu.codlab.lorcana.rph.sync.standings.UserEventStatus

fun EventMatchPlayer.toSync(
    parent: UserEventStatus? = null,
    cached: EventMatchPlayer? = null
): eu.codlab.lorcana.rph.sync.user.User {
    println("original EventMatchPlayer $this")
    println("    user will have bestIdentifierInGame -> ${parent?.bestIdentifier}/${cached?.bestIdentifier}")
    return eu.codlab.lorcana.rph.sync.user.User(
        id = id,
        pronouns = pronouns,
        bestIdentifier = bestIdentifier,
        bestIdentifierInGame = parent?.bestIdentifier ?: cached?.bestIdentifier,
        gameUserProfilePictureUrl = gameUserProfilePictureUrl,
    )
}

fun eu.codlab.lorcana.rph.sync.user.User.isEquals(
    other: EventMatchPlayer,
    parent: UserEventStatus? = null
): Boolean {
    if (id != other.id) return false
    if (pronouns != other.pronouns) return false
    parent?.let { nonNullParent ->
        if (bestIdentifierInGame != nonNullParent.bestIdentifier) return false
    }
    // if (fullProfilePictureUrl != other.fullProfilePictureUrl) return false

    // not checking the in game one as it's not in this context

    return true
}
