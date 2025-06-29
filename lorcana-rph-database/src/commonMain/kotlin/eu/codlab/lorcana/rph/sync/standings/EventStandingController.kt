package eu.codlab.lorcana.rph.sync.standings

import eu.codlab.lorcana.rph.sync.database.AppDatabase
import eu.codlab.lorcana.rph.sync.round.Round

interface EventStandingController {
    suspend fun getAll(): List<EventStanding>

    suspend fun getAll(round: Round, player: Long): List<EventStanding>

    suspend fun get(round: Round, player: Long): EventStanding

    suspend fun insert(eventStanding: EventStanding)

    suspend fun update(eventStanding: EventStanding)

    suspend fun deleteEventStandings()
}

internal class EventStandingControllerImpl(database: AppDatabase) : EventStandingController {
    private val dao = database.getEventStandings()

    override suspend fun getAll() = dao.getAll()

    override suspend fun getAll(round: Round, player: Long) = dao.getAllFromRound(round.id, player)

    override suspend fun get(round: Round, player: Long) = dao.get(round.id, player)

    override suspend fun insert(eventStanding: EventStanding) = dao.insert(eventStanding)

    override suspend fun update(eventStanding: EventStanding) = dao.update(eventStanding)

    override suspend fun deleteEventStandings() = dao.deleteEventStandings()
}
