package eu.codlab.lorcana.rph.sync

import eu.codlab.lorcana.rph.sync.database.SyncDatabase
import eu.codlab.lorcana.rph.sync.event.Event
import eu.codlab.lorcana.rph.sync.extensions.isEquals
import eu.codlab.lorcana.rph.sync.extensions.toSyncEvent

internal class EventWrapper :
    AbstractWrapper<Event, Long, eu.codlab.lorcana.rph.event.Event<*>, Unit, Unit>() {
    private val events = SyncDatabase.events

    override fun getParentKey(model: Event) {
        // nothing
    }

    override suspend fun list() = events.getAll()

    override fun id(fromApi: eu.codlab.lorcana.rph.event.Event<*>) = fromApi.id

    override suspend fun insert(copy: Event) = events.insert(copy)

    override suspend fun update(copy: Event) = events.update(copy)

    override suspend fun toSync(
        fromApi: eu.codlab.lorcana.rph.event.Event<*>,
        cached: Event?,
        foreignParent: Unit?
    ) = fromApi.toSyncEvent()

    override suspend fun isEquals(
        cached: Event,
        fromApi: eu.codlab.lorcana.rph.event.Event<*>
    ) = cached.isEquals(fromApi)
}