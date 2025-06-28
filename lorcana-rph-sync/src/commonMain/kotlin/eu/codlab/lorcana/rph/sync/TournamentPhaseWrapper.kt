package eu.codlab.lorcana.rph.sync

import eu.codlab.lorcana.rph.sync.database.SyncDatabase
import eu.codlab.lorcana.rph.sync.event.Event
import eu.codlab.lorcana.rph.sync.extensions.isEquals
import eu.codlab.lorcana.rph.sync.extensions.toSync
import eu.codlab.lorcana.rph.sync.phases.TournamentPhase

internal class TournamentPhaseWrapper : AbstractWrapper<TournamentPhase,
        Long,
        eu.codlab.lorcana.rph.event.TournamentPhase,
        Event,
        Long>() {
    private val stores = SyncDatabase.tournamentPhases

    override fun getParentKey(model: TournamentPhase) = model.eventId

    override suspend fun list() = stores.getAll()
    override fun id(fromApi: eu.codlab.lorcana.rph.event.TournamentPhase, parent: Event?) =
        fromApi.id

    override suspend fun insert(copy: TournamentPhase) = stores.insert(copy)

    override suspend fun update(copy: TournamentPhase) = stores.update(copy)

    override suspend fun toSync(
        fromApi: eu.codlab.lorcana.rph.event.TournamentPhase,
        cached: TournamentPhase?,
        foreignParent: Event?
    ) = fromApi.toSync(foreignParent!!)

    override suspend fun isEquals(
        cached: TournamentPhase,
        fromApi: eu.codlab.lorcana.rph.event.TournamentPhase
    ) = cached.isEquals(fromApi)
}