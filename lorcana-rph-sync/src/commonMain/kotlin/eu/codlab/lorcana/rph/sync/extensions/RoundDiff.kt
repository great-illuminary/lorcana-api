package eu.codlab.lorcana.rph.sync.extensions

import eu.codlab.lorcana.rph.event.Round
import eu.codlab.lorcana.rph.sync.phases.TournamentPhase

fun Round.toSync(tournamentPhase: TournamentPhase) =
    eu.codlab.lorcana.rph.sync.round.Round(
        id = id,
        tournamentPhaseId = tournamentPhase.id,
        roundNumber = roundNumber,
        finalRoundInEvent = finalRoundInEvent,
        pairingsStatus = pairingsStatus,
        standingsStatus = standingsStatus,
        roundType = roundType,
        status = status,
    )

fun eu.codlab.lorcana.rph.sync.round.Round.isEquals(other: Round): Boolean {
    if (id != other.id) return false

    if (status != other.status) return false
    if (roundNumber != other.roundNumber) return false
    if (finalRoundInEvent != other.finalRoundInEvent) return false
    if (pairingsStatus != other.pairingsStatus) return false
    if (standingsStatus != other.standingsStatus) return false
    if (roundType != other.roundType) return false

    return true
}

