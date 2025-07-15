package eu.codlab.lorcana.rph.sync

import eu.codlab.lorcana.rph.store.StoreFull
import eu.codlab.lorcana.rph.store.StoreFullRestLine
import eu.codlab.lorcana.rph.sync.database.SyncDatabase
import eu.codlab.lorcana.rph.sync.extensions.isEquals
import eu.codlab.lorcana.rph.sync.extensions.toSyncStore
import eu.codlab.lorcana.rph.sync.store.Store

internal class StoreWrapper :
    AbstractWrapper<Store,
            Long,
            StoreFull,
            StoreFullRestLine,
            Unit>() { // parent key is not known unfortunately
    private val stores = SyncDatabase.stores

    override fun getParentKey(model: Store) {
        // nothing
    }

    override suspend fun list() = stores.getAll()

    override fun id(
        fromApi: StoreFull,
        parent: StoreFullRestLine?
    ) = fromApi.id

    override suspend fun insert(copy: Store) = stores.insert(copy)

    override suspend fun update(copy: Store) = stores.update(copy)

    override suspend fun toSync(
        fromApi:StoreFull,
        cached: Store?,
        foreignParent: StoreFullRestLine?
    ) = fromApi.toSyncStore(cached, foreignParent)

    override suspend fun isEquals(cached: Store, fromApi: StoreFull): Boolean {
        TODO("Shouldn't have been called")
    }

    override suspend fun isEquals(
        cached: Store,
        fromApi: StoreFull,
        parent: StoreFullRestLine?
    ) = cached.isEquals(fromApi, parent)
}
