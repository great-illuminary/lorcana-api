package eu.codlab.lorcana.rph.sync.gameplay

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GameplayFormatDao {
    @Query("SELECT * FROM gameplayformat")
    fun flow(): Flow<List<GameplayFormat>>

    @Query("SELECT * FROM gameplayformat")
    suspend fun getAll(): List<GameplayFormat>

    @Insert
    suspend fun insert(gameplayFormat: GameplayFormat)

    @Update
    suspend fun update(gameplayFormat: GameplayFormat)

    @Query("DELETE FROM gameplayformat")
    suspend fun deleteGameplayFormat()
}
