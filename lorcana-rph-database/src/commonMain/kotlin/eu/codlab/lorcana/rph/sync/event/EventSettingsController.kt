package eu.codlab.lorcana.rph.sync.event

import eu.codlab.lorcana.rph.sync.database.AppDatabase

interface EventSettingsController {
    suspend fun getAll(): List<EventSettings>

    suspend fun get(id: Long): EventSettings

    suspend fun insert(eventSettings: EventSettings)

    suspend fun update(eventSettings: EventSettings)

    suspend fun deleteEventSettings()
}

internal class EventSettingsControllerImpl(database: AppDatabase) : EventSettingsController {
    private val dao = database.getEventSettingsDao()

    override suspend fun getAll() = dao.getAll()

    override suspend fun get(id: Long) = dao.get(id)

    override suspend fun insert(eventSettings: EventSettings) = dao.insert(eventSettings)

    override suspend fun update(eventSettings: EventSettings) = dao.update(eventSettings)

    override suspend fun deleteEventSettings() = dao.deleteEventSettings()
}
