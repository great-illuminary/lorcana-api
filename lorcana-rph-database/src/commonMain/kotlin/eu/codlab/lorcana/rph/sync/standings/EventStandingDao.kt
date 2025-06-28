package eu.codlab.lorcana.rph.sync.standings

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface EventStandingDao {
    @Query("SELECT * FROM eventstanding")
    fun flow(): Flow<List<EventStanding>>

    @Query("SELECT * FROM eventstanding")
    suspend fun getAll(): List<EventStanding>

    @Query("SELECT * FROM eventstanding where roundId = :roundId AND playerId = :playerId")
    suspend fun getAllFromRound(roundId: Long, playerId: Long): List<EventStanding>

    @Query("SELECT * FROM eventstanding where roundId = :roundId AND playerId = :playerId LIMIT 1")
    suspend fun get(roundId: Long, playerId: Long): EventStanding

    @Insert
    suspend fun insert(eventStanding: EventStanding)

    @Update
    suspend fun update(eventStanding: EventStanding)

    @Query("DELETE FROM eventstanding")
    suspend fun deleteEventStandings()
}