package eu.codlab.lorcana.rph.sync.round

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RoundDao {
    @Query("SELECT * FROM round")
    fun flow(): Flow<List<Round>>

    @Query("SELECT * FROM round")
    suspend fun getAll(): List<Round>

    @Query("SELECT * FROM round where tournamentPhaseId = :tournamentPhaseId")
    suspend fun getAllFromTournamentPhase(tournamentPhaseId: Long): List<Round>

    @Query("SELECT * FROM round where id = :id LIMIT 1")
    suspend fun get(id: Long): Round

    @Insert
    suspend fun insert(round: Round)

    @Update
    suspend fun update(round: Round)

    @Query("DELETE FROM round")
    suspend fun deleteRounds()
}