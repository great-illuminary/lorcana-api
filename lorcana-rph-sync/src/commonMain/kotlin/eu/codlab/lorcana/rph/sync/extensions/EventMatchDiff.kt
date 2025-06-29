package eu.codlab.lorcana.rph.sync.extensions

import eu.codlab.lorcana.rph.rounds.matches.EventMatch

fun EventMatch.toSync() = eu.codlab.lorcana.rph.sync.match.EventMatch(
    id = id,
    createdAt = createdAt,
    updatedAt = updatedAt,
    tableNumber = tableNumber,
    order = order,
    status = status,
    podNumber = podNumber,
    matchIsIntentionalDraw = matchIsIntentionalDraw,
    matchIsUnintentionalDraw = matchIsUnintentionalDraw,
    matchIsBye = matchIsBye,
    matchIsLoss = matchIsLoss,
    reportsAreInConflict = reportsAreInConflict,
    gamesDrawn = gamesDrawn,
    gamesWonByWinner = gamesWonByWinner,
    gamesWonByLoser = gamesWonByLoser,
    isGhostMatch = isGhostMatch,
    isFeatureMatch = isFeatureMatch,
    deckCheckStarted = deckCheckStarted,
    deckCheckCompleted = deckCheckCompleted,
    timeExtensionSeconds = timeExtensionSeconds,
    teamEventMatch = teamEventMatch,
    tournamentRound = tournamentRound,
    reportingPlayer = reportingPlayer,
    winningPlayer = winningPlayer,
    assignedJudge = assignedJudge,
    player1 = player1(),
    player1Order = player1Order(),
    player2 = player2(),
    player2Order = player2Order(),
)

fun eu.codlab.lorcana.rph.sync.match.EventMatch.isEquals(other: EventMatch): Boolean {
    if (id != other.id) return false
    if (createdAt != other.createdAt) return false
    if (updatedAt != other.updatedAt) return false
    if (tableNumber != other.tableNumber) return false
    if (order != other.order) return false
    if (status != other.status) return false
    if (podNumber != other.podNumber) return false
    if (matchIsIntentionalDraw != other.matchIsIntentionalDraw) return false
    if (matchIsUnintentionalDraw != other.matchIsUnintentionalDraw) return false
    if (matchIsBye != other.matchIsBye) return false
    if (matchIsLoss != other.matchIsLoss) return false
    if (reportsAreInConflict != other.reportsAreInConflict) return false
    if (gamesDrawn != other.gamesDrawn) return false
    if (gamesWonByWinner != other.gamesWonByWinner) return false
    if (gamesWonByLoser != other.gamesWonByLoser) return false
    if (isGhostMatch != other.isGhostMatch) return false
    if (isFeatureMatch != other.isFeatureMatch) return false
    if (deckCheckStarted != other.deckCheckStarted) return false
    if (deckCheckCompleted != other.deckCheckCompleted) return false
    if (timeExtensionSeconds != other.timeExtensionSeconds) return false
    if (teamEventMatch != other.teamEventMatch) return false
    if (tournamentRound != other.tournamentRound) return false
    if (reportingPlayer != other.reportingPlayer) return false
    if (winningPlayer != other.winningPlayer) return false
    if (assignedJudge != other.assignedJudge) return false
    if (player1 != other.player1()) return false
    if (player1Order != other.player1Order()) return false
    if (player2 != other.player2()) return false
    if (player2Order != other.player2Order()) return false

    return true
}
