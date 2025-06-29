package eu.codlab.lorcana.rph.sync

import eu.codlab.lorcana.rph.sync.database.SyncDatabase
import eu.codlab.lorcana.rph.sync.extensions.isEquals
import eu.codlab.lorcana.rph.sync.extensions.toSyncStore
import eu.codlab.lorcana.rph.sync.store.Store

internal class StoreWrapper :
    AbstractWrapper<Store, Long, eu.codlab.lorcana.rph.store.StoreFull, Unit, Unit>() {
    private val stores = SyncDatabase.stores

    override fun getParentKey(model: Store) {
        // nothing
    }

    override suspend fun list() = stores.getAll()
    override fun id(fromApi: eu.codlab.lorcana.rph.store.StoreFull, parent: Unit?) = fromApi.id

    override suspend fun insert(copy: Store) = stores.insert(copy)

    override suspend fun update(copy: Store) = stores.update(copy)

    override suspend fun toSync(
        fromApi: eu.codlab.lorcana.rph.store.StoreFull,
        cached: Store?,
        foreignParent: Unit?
    ) = fromApi.toSyncStore(cached)

    override suspend fun isEquals(
        cached: Store,
        fromApi: eu.codlab.lorcana.rph.store.StoreFull
    ) = cached.isEquals(fromApi)
}