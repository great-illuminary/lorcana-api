package eu.codlab.lorcana.rph.sync.store

import eu.codlab.lorcana.rph.sync.database.AppDatabase

interface StoreController {
    suspend fun getAll(): List<Store>

    suspend fun insert(store: Store)

    suspend fun update(store: Store)

    suspend fun deleteStores()
}

internal class StoreControllerImpl(database: AppDatabase) : StoreController {
    private val dao = database.getStoreDao()

    override suspend fun getAll() = dao.getAll()

    override suspend fun insert(store: Store) = dao.insert(store)

    override suspend fun update(store: Store) = dao.update(store)

    override suspend fun deleteStores() = dao.deleteStores()
}
