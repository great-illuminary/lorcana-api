package eu.codlab.lorcana.rph.sync.settings

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingDao {
    @Query("SELECT * FROM setting")
    fun flow(): Flow<List<Setting>>

    @Query("SELECT * FROM setting")
    suspend fun getAll(): List<Setting>

    @Query("SELECT * FROM setting WHERE \"key\" = :key")
    suspend fun get(key: String): Setting?

    @Insert
    suspend fun insert(setting: Setting)

    @Update
    suspend fun update(setting: Setting)

    @Query("DELETE FROM setting")
    suspend fun deleteSettings()
}
