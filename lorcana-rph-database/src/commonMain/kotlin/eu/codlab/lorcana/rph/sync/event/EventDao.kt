package eu.codlab.lorcana.rph.sync.event

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query("SELECT * FROM event")
    fun flow(): Flow<List<Event>>

    @Query("SELECT * FROM event")
    suspend fun getAll(): List<Event>

    @Query(
        """
        SELECT event.id FROM event
        LEFT JOIN usereventstatus as ues ON ues.eventId = event.id
        WHERE ues.userId = :userId"""
    )
    suspend fun getAllForUser(userId: Long): List<Long>

    @Query(
        """
        SELECT event.id FROM event
        WHERE event.storeId = :storeId"""
    )
    suspend fun getAllForStore(storeId: Long): List<Long>

    @Query("SELECT * FROM event WHERE id = :id")
    suspend fun get(id: Long): Event

    @Insert
    suspend fun insert(event: Event)

    @Update
    suspend fun update(event: Event)

    @Query("DELETE FROM event")
    suspend fun deleteEvents()
}
