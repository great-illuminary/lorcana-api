package eu.codlab.lorcana.rph.sync.extensions

import eu.codlab.lorcana.rph.event.TournamentPhase
import eu.codlab.lorcana.rph.sync.event.Event

fun TournamentPhase.toSync(event: Event) =
    eu.codlab.lorcana.rph.sync.phases.TournamentPhase(
        id = id,
        firstRoundType = firstRoundType,
        status = status,
        orderInPhases = orderInPhases,
        numberOfRounds = numberOfRounds,
        roundType = roundType,
        rankRequiredToEnterPhase = rankRequiredToEnterPhase,
        eventId = event.id
    )

fun eu.codlab.lorcana.rph.sync.phases.TournamentPhase.isEquals(other: TournamentPhase): Boolean {
    if (id != other.id) return false

    if (firstRoundType != other.firstRoundType) return false
    if (status != other.status) return false
    if (orderInPhases != other.orderInPhases) return false
    if (numberOfRounds != other.numberOfRounds) return false
    if (roundType != other.roundType) return false
    if (rankRequiredToEnterPhase != other.rankRequiredToEnterPhase) return false

    return true
}
