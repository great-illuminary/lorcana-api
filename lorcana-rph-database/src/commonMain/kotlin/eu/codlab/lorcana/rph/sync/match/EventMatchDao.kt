package eu.codlab.lorcana.rph.sync.match

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface EventMatchDao {
    @Query("SELECT * FROM eventmatch")
    fun flow(): Flow<List<EventMatch>>

    @Query("SELECT * FROM eventmatch")
    suspend fun getAll(): List<EventMatch>

    @Query("SELECT * FROM eventmatch where tournamentRound = :roundId")
    suspend fun getAllFromRound(roundId: Long): List<EventMatch>

    @Insert
    suspend fun insert(eventMatch: EventMatch)

    @Update
    suspend fun update(eventMatch: EventMatch)

    @Query("DELETE FROM eventmatch")
    suspend fun deleteEventMatches()
}