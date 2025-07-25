package eu.codlab.lorcana.rph.sync.standings

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserEventStatusDao {
    @Query("SELECT * FROM usereventstatus")
    fun flow(): Flow<List<UserEventStatus>>

    @Query("SELECT * FROM usereventstatus")
    suspend fun getAll(): List<UserEventStatus>

    @Query("SELECT * FROM usereventstatus where eventId = :eventId AND userId = :userId")
    suspend fun getAllFromEventStanding(eventId: Long, userId: Long): List<UserEventStatus>

    @Query("SELECT * FROM usereventstatus where eventId = :eventId AND userId = :userId LIMIT 1")
    suspend fun get(eventId: Long, userId: Long): UserEventStatus

    @Insert
    suspend fun insert(userEventStatus: UserEventStatus)

    @Update
    suspend fun update(userEventStatus: UserEventStatus)

    @Query("DELETE FROM usereventstatus")
    suspend fun deleteUserEventStatus()
}
