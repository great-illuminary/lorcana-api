package eu.codlab.lorcana.rph.sync

import eu.codlab.lorcana.rph.sync.database.SyncDatabase
import eu.codlab.lorcana.rph.sync.extensions.isEquals
import eu.codlab.lorcana.rph.sync.extensions.toSync
import eu.codlab.lorcana.rph.sync.match.EventMatch
import eu.codlab.lorcana.rph.sync.round.Round

internal class EventMatchWrapper : AbstractWrapper<EventMatch,
        Long,
        eu.codlab.lorcana.rph.rounds.matches.EventMatch,
        Round,
        Long>() {
    private val stores = SyncDatabase.eventMatches

    override fun getParentKey(model: EventMatch) = model.tournamentRound

    override suspend fun list() = stores.getAll()
    override fun id(
        fromApi: eu.codlab.lorcana.rph.rounds.matches.EventMatch,
        parent: Round?
    ) = fromApi.id

    override suspend fun insert(copy: EventMatch) = stores.insert(copy)

    override suspend fun update(copy: EventMatch) = stores.update(copy)

    override suspend fun toSync(
        fromApi: eu.codlab.lorcana.rph.rounds.matches.EventMatch,
        cached: EventMatch?,
        foreignParent: Round?
    ) = fromApi.toSync()

    override suspend fun isEquals(
        cached: EventMatch,
        fromApi: eu.codlab.lorcana.rph.rounds.matches.EventMatch
    ) = cached.isEquals(fromApi)
}