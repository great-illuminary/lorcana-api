package eu.codlab.lorcana.rph.sync

import eu.codlab.lorcana.rph.sync.database.SyncDatabase
import eu.codlab.lorcana.rph.sync.extensions.isEquals
import eu.codlab.lorcana.rph.sync.extensions.toSync
import eu.codlab.lorcana.rph.sync.phases.TournamentPhase
import eu.codlab.lorcana.rph.sync.round.Round

internal class RoundWrapper : AbstractWrapper<
        Round,
        Long,
        eu.codlab.lorcana.rph.event.Round,
        TournamentPhase,
        Long
        >() {
    private val rounds = SyncDatabase.rounds

    override fun getParentKey(model: Round): Long {
        return model.tournamentPhaseId
    }

    override suspend fun list() = rounds.getAll()

    override fun id(fromApi: eu.codlab.lorcana.rph.event.Round, parent: TournamentPhase?) =
        fromApi.id

    override suspend fun insert(copy: Round) = rounds.insert(copy)

    override suspend fun update(copy: Round) = rounds.update(copy)

    override suspend fun toSync(
        fromApi: eu.codlab.lorcana.rph.event.Round,
        cached: Round?,
        foreignParent: TournamentPhase?
    ) = fromApi.toSync(foreignParent!!)

    override suspend fun isEquals(
        cached: Round,
        fromApi: eu.codlab.lorcana.rph.event.Round
    ) = cached.isEquals(fromApi)
}
