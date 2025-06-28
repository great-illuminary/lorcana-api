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
                tournamentPhases = synchronizer.tournamentPhaseAccess.getFromParent(it.id),
                gameplayFormat = synchronizer.gameplayFormatAccess.getFromId(it.gameplayFormatId)
            )
        }
    }

    fun directApiAccess() = loader

    suspend fun startSync() {
        synchronizer.start()
    }
}