package eu.codlab.lorcana.rph.sync.event

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface EventSettingsDao {
    @Query("SELECT * FROM eventsettings")
    fun flow(): Flow<List<EventSettings>>

    @Query("SELECT * FROM eventsettings")
    suspend fun getAll(): List<EventSettings>

    @Query("SELECT * FROM eventsettings WHERE id = :id")
    suspend fun get(id: Long): EventSettings

    @Insert
    suspend fun insert(eventSettings: EventSettings)

    @Update
    suspend fun update(eventSettings: EventSettings)

    @Query("DELETE FROM eventsettings")
    suspend fun deleteEventSettings()
}