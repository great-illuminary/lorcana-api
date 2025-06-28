package eu.codlab.lorcana.rph.sync

import eu.codlab.lorcana.rph.sync.database.SyncDatabase
import eu.codlab.lorcana.rph.sync.event.Event
import eu.codlab.lorcana.rph.sync.extensions.isEquals
import eu.codlab.lorcana.rph.sync.extensions.toSync
import eu.codlab.lorcana.rph.sync.overrides.UserEventStatusParent
import eu.codlab.lorcana.rph.sync.phases.TournamentPhase
import eu.codlab.lorcana.rph.sync.round.Round
import eu.codlab.lorcana.rph.sync.standings.EventStanding
import eu.codlab.lorcana.rph.sync.standings.UserEventStatus

internal class UserEventStatusWrapper : AbstractWrapper<UserEventStatus,
        Long,
        eu.codlab.lorcana.rph.rounds.standings.UserEventStatus,
        UserEventStatusParent,
        Long>() {
    private val stores = SyncDatabase.userEventStatus

    override fun getParentKey(model: UserEventStatus) = model.eventId

    override suspend fun list() = stores.getAll()

    override fun id(
        fromApi: eu.codlab.lorcana.rph.rounds.standings.UserEventStatus,
        parent: UserEventStatusParent?
    ) = fromApi.id

    override suspend fun insert(copy: UserEventStatus) = stores.insert(copy)

    override suspend fun update(copy: UserEventStatus) = stores.update(copy)

    override suspend fun toSync(
        fromApi: eu.codlab.lorcana.rph.rounds.standings.UserEventStatus,
        cached: UserEventStatus?,
        foreignParent: UserEventStatusParent?
    ) = fromApi.toSync(foreignParent!!)

    override suspend fun isEquals(
        cached: UserEventStatus,
        fromApi: eu.codlab.lorcana.rph.rounds.standings.UserEventStatus
    )= cached.isEquals(fromApi)
}