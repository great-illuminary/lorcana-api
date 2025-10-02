package eu.codlab.lorcana.rph.sync.app

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/*@Entity(
    indices = [
        Index(value = ["appToken"]),
    ]
)*/
@Serializable
data class RegisteredApp(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String = "",
    val appToken: String,
)
