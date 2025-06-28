package eu.codlab.lorcana.rph.sync.extensions

import eu.codlab.lorcana.rph.rounds.standings.UserEventStatus
import eu.codlab.lorcana.rph.sync.round.Round

fun UserEventStatus.toSync(
    standing: eu.codlab.lorcana.rph.sync.standings.EventStanding
) = eu.codlab.lorcana.rph.sync.standings.UserEventStatus(
    id = id,
    matchesWon = matchesWon,
    matchesDrawn = matchesDrawn,
    matchesLost = matchesLost,
    totalMatchPoints = totalMatchPoints,
    registrationStatus = registrationStatus,
    fullProfilePictureUrl = fullProfilePictureUrl,
    bestIdentifier = bestIdentifier,
    roundId = standing.roundId,
    userId = this.user?.id ?: standing.playerId,
)

fun eu.codlab.lorcana.rph.sync.standings.UserEventStatus.isEquals(other: UserEventStatus): Boolean {
    if (id != other.id) return false
    if (matchesWon != other.matchesWon) return false
    if (matchesDrawn != other.matchesDrawn) return false
    if (matchesLost != other.matchesLost) return false
    if (totalMatchPoints != other.totalMatchPoints) return false
    if (registrationStatus != other.registrationStatus) return false
    if (fullProfilePictureUrl != other.fullProfilePictureUrl) return false
    if (bestIdentifier != other.bestIdentifier) return false
    // if (roundId != other.player.id) return false
    // if (userId != other.player.id) return false

    return true
}

