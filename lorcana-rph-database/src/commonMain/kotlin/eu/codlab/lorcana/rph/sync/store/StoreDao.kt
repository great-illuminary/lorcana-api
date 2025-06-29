package eu.codlab.lorcana.rph.sync.store

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreDao {
    @Query("SELECT * FROM store")
    fun flow(): Flow<List<Store>>

    @Query("SELECT * FROM store")
    suspend fun getAll(): List<Store>

    @Insert
    suspend fun insert(store: Store)

    @Update
    suspend fun update(store: Store)

    @Query("DELETE FROM store")
    suspend fun deleteStores()
}
