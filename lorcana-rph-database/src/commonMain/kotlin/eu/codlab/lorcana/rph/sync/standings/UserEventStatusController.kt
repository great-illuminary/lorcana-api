package eu.codlab.lorcana.rph.sync.standings

import eu.codlab.lorcana.rph.sync.database.AppDatabase
import eu.codlab.lorcana.rph.sync.round.Round

interface UserEventStatusController {
    suspend fun getAll(): List<UserEventStatus>

    suspend fun getAll(round: Round, player: Long): List<UserEventStatus>

    suspend fun get(round: Round, player: Long): UserEventStatus

    suspend fun insert(userEventStatus: UserEventStatus)

    suspend fun update(userEventStatus: UserEventStatus)

    suspend fun deleteEventStandings()
}

internal class UserEventStatusControllerImpl(database: AppDatabase) : UserEventStatusController {
    private val dao = database.getUserEventStatus()

    override suspend fun getAll() = dao.getAll()

    override suspend fun getAll(round: Round, player: Long) = dao.getAllFromEventStanding(round.id, player)

    override suspend fun get(round: Round, player: Long) = dao.get(round.id, player)

    override suspend fun insert(userEventStatus: UserEventStatus) = dao.insert(userEventStatus)

    override suspend fun update(userEventStatus: UserEventStatus) = dao.update(userEventStatus)

    override suspend fun deleteEventStandings() = dao.deleteUserEventStatus()
}