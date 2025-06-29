package eu.codlab.lorcana.rph.sync.phases

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TournamentPhaseDao {
    @Query("SELECT * FROM tournamentphase")
    fun flow(): Flow<List<TournamentPhase>>

    @Query("SELECT * FROM tournamentphase")
    suspend fun getAll(): List<TournamentPhase>

    @Query("SELECT * FROM tournamentphase WHERE eventId = :event")
    suspend fun getAll(event: Long): List<TournamentPhase>

    @Insert
    suspend fun insert(tournamentPhase: TournamentPhase)

    @Update
    suspend fun update(tournamentPhase: TournamentPhase)

    @Query("DELETE FROM round")
    suspend fun deleteTournamentPhases()
}
