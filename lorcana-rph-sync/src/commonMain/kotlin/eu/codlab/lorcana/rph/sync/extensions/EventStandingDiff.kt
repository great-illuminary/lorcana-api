package eu.codlab.lorcana.rph.sync.extensions

import eu.codlab.lorcana.rph.rounds.standings.EventStanding
import eu.codlab.lorcana.rph.sync.round.Round

fun EventStanding.toSync(
    round: Round,
    // userEventStatus: eu.codlab.lorcana.rph.sync.standings.UserEventStatus
) = eu.codlab.lorcana.rph.sync.standings.EventStanding(
    rank = rank,
    record = record,
    matchRecord = matchRecord,
    matchPoints = matchPoints,
    opponentMatchWinPercentage = opponentMatchWinPercentage,
    opponentGameWinPercentage = opponentGameWinPercentage,

    roundId = round.id,
    playerId = player.id,
    // userEventStatusId = userEventStatus.id
)

fun eu.codlab.lorcana.rph.sync.standings.EventStanding.isEquals(other: EventStanding): Boolean {
    if (rank != other.rank) return false
    if (record != other.record) return false
    if (matchRecord != other.matchRecord) return false
    if (matchPoints != other.matchPoints) return false
    if (opponentMatchWinPercentage != other.opponentMatchWinPercentage) return false
    if (opponentGameWinPercentage != other.opponentGameWinPercentage) return false
    if (playerId != other.player.id) return false
    // if (userEventStatusId != other.userEventStatus?.id) return false

    return true
}

