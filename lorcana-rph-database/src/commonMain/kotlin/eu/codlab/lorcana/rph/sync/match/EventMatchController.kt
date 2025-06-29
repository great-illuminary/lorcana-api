package eu.codlab.lorcana.rph.sync.match

import eu.codlab.lorcana.rph.sync.database.AppDatabase
import eu.codlab.lorcana.rph.sync.round.Round

interface EventMatchController {
    suspend fun getAll(): List<EventMatch>

    suspend fun getAll(round: Round): List<EventMatch>

    suspend fun insert(eventMatch: EventMatch)

    suspend fun update(eventMatch: EventMatch)

    suspend fun deleteEventMatches()
}

internal class EventMatchControllerImpl(database: AppDatabase) : EventMatchController {
    private val dao = database.getEventMatches()

    override suspend fun getAll() = dao.getAll()

    override suspend fun getAll(round: Round) = dao.getAllFromRound(round.id)

    override suspend fun insert(eventMatch: EventMatch) = dao.insert(eventMatch)

    override suspend fun update(eventMatch: EventMatch) = dao.update(eventMatch)

    override suspend fun deleteEventMatches() = dao.deleteEventMatches()
}
