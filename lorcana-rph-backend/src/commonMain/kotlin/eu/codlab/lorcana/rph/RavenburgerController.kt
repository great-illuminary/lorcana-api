package eu.codlab.lorcana.rph

import eu.codlab.lorcana.rph.sync.Sync

class RavenburgerController {
    private val loader = LoadRPHCall()
    private val synchronizer = Sync()

    fun events(): List<EventHolder> {
        val events = synchronizer.eventAccess.getCachedList()

        return events.map {
            EventHolder(
                event = it,
                settings = synchronizer.settingsAccess.getFromId(it.settingsId)!!,
                userEventStatus = synchronizer.userEventStatusAccess.getFromParent(it.id),
                tournamentPhases = synchronizer.tournamentPhaseAccess.getFromParent(it.id)
                    .map { phase ->
                        TournamentPhaseHolder(
                            phase = phase,
                            rounds = synchronizer.roundAccess.getFromParent(phase.id).map { round ->
                                RoundHolder(
                                    round = round,
                                    standings = synchronizer.eventStandingAccess.getFromParent(round.id)
                                )
                            }
                        )
                    },
                gameplayFormat = synchronizer.gameplayFormatAccess.getFromId(it.gameplayFormatId)
            )
        }
    }

    fun directApiAccess() = loader

    suspend fun startSync() {
        synchronizer.start()
    }
}