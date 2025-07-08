package eu.codlab.lorcana.rph.sync.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun flow(): Flow<List<User>>

    @Query("SELECT * FROM user")
    suspend fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE bestIdentifier LIKE :matching OR bestIdentifierInGame LIKE :matching")
    suspend fun getAll(matching: String): List<User>

    @Query(
        """
            SELECT user.id AS id, ues.bestIdentifier AS bestIdentifierInGame FROM user
            LEFT JOIN usereventstatus AS ues ON ues.userId=user.id
            WHERE bestIdentifierInGame IS NULL AND ues.bestIdentifier IS NOT NULL
           """
    )
    suspend fun getAllToFix(): List<UserToFixName>

    @Insert
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Query("DELETE FROM user")
    suspend fun deleteUsers()
}
