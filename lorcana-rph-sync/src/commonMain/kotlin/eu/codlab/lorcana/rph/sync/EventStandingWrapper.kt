package eu.codlab.lorcana.rph.sync

import eu.codlab.lorcana.rph.sync.database.SyncDatabase
import eu.codlab.lorcana.rph.sync.extensions.isEquals
import eu.codlab.lorcana.rph.sync.extensions.toSync
import eu.codlab.lorcana.rph.sync.round.Round
import eu.codlab.lorcana.rph.sync.standings.EventStanding

internal class EventStandingWrapper : AbstractWrapper<
        EventStanding,
        String,
        eu.codlab.lorcana.rph.rounds.standings.EventStanding,
        Round,
        Long
        >() {
    private val stores = SyncDatabase.eventStandings

    override fun getParentKey(model: EventStanding) = model.roundId

    override suspend fun list() = stores.getAll()

    override fun id(
        fromApi: eu.codlab.lorcana.rph.rounds.standings.EventStanding,
        parent: Round?
    ) = "${parent?.id}_${fromApi.player!!.id}"

    override suspend fun insert(copy: EventStanding) = stores.insert(copy)

    override suspend fun update(copy: EventStanding) = stores.update(copy)

    override suspend fun toSync(
        fromApi: eu.codlab.lorcana.rph.rounds.standings.EventStanding,
        cached: EventStanding?,
        foreignParent: Round?
    ) = fromApi.toSync(foreignParent!!)

    override suspend fun isEquals(
        cached: EventStanding,
        fromApi: eu.codlab.lorcana.rph.rounds.standings.EventStanding
    ) = cached.isEquals(fromApi)
}
