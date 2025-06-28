package eu.codlab.lorcana.rph.sync.round

import eu.codlab.lorcana.rph.sync.database.AppDatabase
import eu.codlab.lorcana.rph.sync.phases.TournamentPhase

interface RoundController {
    suspend fun getAll(): List<Round>

    suspend fun getAll(tournamentPhase: TournamentPhase): List<Round>

    suspend fun get(id: Long): Round

    suspend fun insert(round: Round)

    suspend fun update(round: Round)

    suspend fun deleteRounds()
}

internal class RoundControllerImpl(database: AppDatabase) : RoundController {
    private val dao = database.getRoundDao()

    override suspend fun getAll() = dao.getAll()

    override suspend fun getAll(tournamentPhase: TournamentPhase) =
        dao.getAllFromTournamentPhase(tournamentPhase.id)

    override suspend fun get(id: Long) = dao.get(id)

    override suspend fun insert(round: Round) = dao.insert(round)

    override suspend fun update(round: Round) = dao.update(round)

    override suspend fun deleteRounds() = dao.deleteRounds()
}