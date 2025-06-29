package eu.codlab.lorcana.rph.sync.standings

import eu.codlab.lorcana.rph.sync.database.AppDatabase
import eu.codlab.lorcana.rph.sync.event.Event

interface UserEventStatusController {
    suspend fun getAll(): List<UserEventStatus>

    suspend fun getAll(event: Event, player: Long): List<UserEventStatus>

    suspend fun get(event: Event, player: Long): UserEventStatus

    suspend fun insert(userEventStatus: UserEventStatus)

    suspend fun update(userEventStatus: UserEventStatus)

    suspend fun deleteEventStandings()
}

internal class UserEventStatusControllerImpl(database: AppDatabase) : UserEventStatusController {
    private val dao = database.getUserEventStatus()

    override suspend fun getAll() = dao.getAll()

    override suspend fun getAll(event: Event, player: Long) =
        dao.getAllFromEventStanding(event.id, player)

    override suspend fun get(event: Event, player: Long) = dao.get(event.id, player)

    override suspend fun insert(userEventStatus: UserEventStatus) = dao.insert(userEventStatus)

    override suspend fun update(userEventStatus: UserEventStatus) = dao.update(userEventStatus)

    override suspend fun deleteEventStandings() = dao.deleteUserEventStatus()
}
