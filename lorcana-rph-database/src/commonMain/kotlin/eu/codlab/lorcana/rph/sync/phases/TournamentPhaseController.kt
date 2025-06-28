package eu.codlab.lorcana.rph.sync.phases

import eu.codlab.lorcana.rph.sync.database.AppDatabase
import eu.codlab.lorcana.rph.sync.event.Event

interface TournamentPhaseController {
    suspend fun getAll(): List<TournamentPhase>

    suspend fun getAll(event: Event): List<TournamentPhase>

    suspend fun insert(tournamentPhase: TournamentPhase)

    suspend fun update(tournamentPhase: TournamentPhase)

    suspend fun deleteTournamentPhases()
}

internal class TournamentPhaseControllerImpl(database: AppDatabase) : TournamentPhaseController {
    private val dao = database.getTournamentPhaseDao()

    override suspend fun getAll() = dao.getAll()

    override suspend fun getAll(event: Event) = dao.getAll(event.id)

    override suspend fun insert(tournamentPhase: TournamentPhase) = dao.insert(tournamentPhase)

    override suspend fun update(tournamentPhase: TournamentPhase) = dao.update(tournamentPhase)

    override suspend fun deleteTournamentPhases() = dao.deleteTournamentPhases()
}