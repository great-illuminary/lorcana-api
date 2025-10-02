package eu.codlab.lorcana.rph.sync.app

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RegisteredAppDao {
    @Query("SELECT * FROM registeredapp")
    fun flow(): Flow<List<RegisteredApp>>

    @Query("SELECT * FROM registeredapp")
    suspend fun getAll(): List<RegisteredApp>

    @Query(
        """
        SELECT id FROM registeredapp
        WHERE registeredapp.appToken = :appToken"""
    )
    suspend fun getIdForToken(appToken: String): Long

    @Query("SELECT * FROM registeredapp WHERE appToken = :appToken")
    suspend fun get(appToken: String): RegisteredApp

    @Insert
    suspend fun insert(app: RegisteredApp)

    @Update
    suspend fun update(app: RegisteredApp)

    @Query("DELETE FROM registeredapp")
    suspend fun deleteRegisteredApps()
}
