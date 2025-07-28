package eu.codlab.lorcana.rph.sync

import eu.codlab.lorcana.rph.sync.database.SyncDatabase
import eu.codlab.lorcana.rph.sync.event.Event
import eu.codlab.lorcana.rph.sync.extensions.isEquals
import eu.codlab.lorcana.rph.sync.extensions.toSyncEvent
import eu.codlab.lorcana.rph.sync.store.Store
import eu.codlab.lorcana.rph.sync.user.User

interface IEventWrapper : CacheAccess<Event, Long, Unit> {
    suspend fun forStoreIdsOnly(store: Store): List<Long>

    suspend fun forUserIdsOnly(user: User): List<Long>

    suspend fun forUser(user: User): List<Event>
}

internal class EventWrapper :
    AbstractWrapper<Event, Long, eu.codlab.lorcana.rph.event.Event, Unit, Unit>(),
    IEventWrapper {
    private val events = SyncDatabase.events

    override fun getParentKey(model: Event) {
        // nothing
    }

    override suspend fun list() = events.getAll()

    override fun id(fromApi: eu.codlab.lorcana.rph.event.Event, parent: Unit?) = fromApi.id

    override suspend fun insert(copy: Event) = events.insert(copy)

    override suspend fun update(copy: Event) = events.update(copy)

    override suspend fun toSync(
        fromApi: eu.codlab.lorcana.rph.event.Event,
        cached: Event?,
        foreignParent: Unit?
    ) = fromApi.toSyncEvent(cached)

    override suspend fun isEquals(
        cached: Event,
        fromApi: eu.codlab.lorcana.rph.event.Event
    ) = cached.isEquals(fromApi)

    suspend fun setInternalDataPostTreatment(
        eventFromDatabase: Event,
        updatedPostEvent: Boolean,
        refreshedAtMilliseconds: Long
    ) {
        val copy = eventFromDatabase.copy(
            updatedPostEvent = updatedPostEvent,
            refreshedAtMilliseconds = refreshedAtMilliseconds
        )

        cache.indexOfFirst { it.modelId() == copy.modelId() }.let { existingId ->
            if (existingId >= 0) {
                cache[existingId] = copy
            } else {
                cache.add(copy)
            }
        }

        cacheMap[copy.id] = copy

        update(copy)
    }

    override suspend fun forStoreIdsOnly(store: Store) = events.getAll(store)

    override suspend fun forUserIdsOnly(user: User) = events.getAll(user)

    override suspend fun forUser(user: User) = forUserIdsOnly(user).mapNotNull { cacheMap[it] }
}
