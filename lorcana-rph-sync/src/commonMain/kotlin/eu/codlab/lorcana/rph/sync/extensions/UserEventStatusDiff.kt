package eu.codlab.lorcana.rph.sync.extensions

import eu.codlab.lorcana.rph.rounds.standings.UserEventStatus
import eu.codlab.lorcana.rph.sync.overrides.UserEventStatusParent

fun UserEventStatus.toSync(
    standing: UserEventStatusParent,
    cached: UserEventStatus? = null
) = eu.codlab.lorcana.rph.sync.standings.UserEventStatus(
    id = id,
    matchesWon = cached?.matchesWon ?: matchesWon,
    matchesDrawn = cached?.matchesDrawn ?: matchesDrawn,
    matchesLost = cached?.matchesLost ?: matchesLost,
    totalMatchPoints = cached?.totalMatchPoints ?: totalMatchPoints,
    registrationStatus = cached?.registrationStatus ?: registrationStatus,
    fullProfilePictureUrl = cached?.fullProfilePictureUrl ?: fullProfilePictureUrl,
    deckId = cached?.deckId ?: deckId,
    bestIdentifier = cached?.bestIdentifier ?: bestIdentifier,
    eventId = standing.eventId,
    userId = this.user?.id ?: standing.playerId,
    specialUserIdentifier = cached?.specialUserIdentifier ?: specialUserIdentifier,
    finalPlaceInStandings = cached?.finalPlaceInStandings ?: finalPlaceInStandings,
    registrationCompletedDatetime = cached?.registrationCompletedDatetime
        ?: registrationCompletedDatetime,
)

fun eu.codlab.lorcana.rph.sync.standings.UserEventStatus.isEquals(other: UserEventStatus): Boolean {
    if (id != other.id) return false
    if (matchesWon != other.matchesWon) return false
    if (matchesDrawn != other.matchesDrawn) return false
    if (matchesLost != other.matchesLost) return false
    if (totalMatchPoints != other.totalMatchPoints) return false
    if (registrationStatus != other.registrationStatus) return false
    // if (fullProfilePictureUrl != other.fullProfilePictureUrl) return false
    if (bestIdentifier != other.bestIdentifier) return false
    // if (roundId != other.player.id) return false
    // if (userId != other.player.id) return false

    other.deckId?.let {
        if (deckId != it) return false
    }

    other.specialUserIdentifier?.let {
        if (specialUserIdentifier != it) return false
    }
    other.finalPlaceInStandings?.let {
        if (finalPlaceInStandings != it) return false
    }
    other.registrationCompletedDatetime?.let {
        if (registrationCompletedDatetime != it) return false
    }

    return true
}
