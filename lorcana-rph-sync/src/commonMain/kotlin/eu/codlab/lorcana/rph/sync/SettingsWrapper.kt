package eu.codlab.lorcana.rph.sync

import eu.codlab.lorcana.rph.sync.database.SyncDatabase
import eu.codlab.lorcana.rph.sync.event.EventSettings
import eu.codlab.lorcana.rph.sync.extensions.isEquals
import eu.codlab.lorcana.rph.sync.extensions.toSync

internal class SettingsWrapper :
    AbstractWrapper<EventSettings, Long, eu.codlab.lorcana.rph.event.EventSettings, Unit, Unit>() {
    private val settings = SyncDatabase.eventSettings

    override fun getParentKey(model: EventSettings) {
        // nothing
    }

    override suspend fun list() = settings.getAll()

    override fun id(fromApi: eu.codlab.lorcana.rph.event.EventSettings, parent: Unit?) = fromApi.id

    override suspend fun insert(copy: EventSettings) = settings.insert(copy)

    override suspend fun update(copy: EventSettings) = settings.update(copy)

    override suspend fun toSync(
        fromApi: eu.codlab.lorcana.rph.event.EventSettings,
        cached: EventSettings?,
        foreignParent: Unit?
    ) = fromApi.toSync()

    override suspend fun isEquals(
        cached: EventSettings,
        fromApi: eu.codlab.lorcana.rph.event.EventSettings
    ) = cached.isEquals(fromApi)
}
