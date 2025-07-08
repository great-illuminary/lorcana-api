package eu.codlab.lorcana.rph.sync.event

import eu.codlab.lorcana.rph.sync.database.AppDatabase
import eu.codlab.lorcana.rph.sync.store.Store
import eu.codlab.lorcana.rph.sync.user.User

interface EventController {
    suspend fun getAll(): List<Event>

    suspend fun getAll(user: User): List<Long>

    suspend fun getAll(store: Store): List<Long>

    suspend fun get(id: Long): Event

    suspend fun insert(event: Event)

    suspend fun update(event: Event)

    suspend fun deleteEvents()
}

internal class EventControllerImpl(database: AppDatabase) : EventController {
    private val dao = database.getEventDao()

    override suspend fun getAll() = dao.getAll()

    override suspend fun getAll(user: User) = dao.getAllForUser(user.id)

    override suspend fun getAll(store: Store) = dao.getAllForStore(store.id)

    override suspend fun get(id: Long) = dao.get(id)

    override suspend fun insert(event: Event) = dao.insert(event)

    override suspend fun update(event: Event) = dao.update(event)

    override suspend fun deleteEvents() = dao.deleteEvents()
}
