package eu.codlab.lorcana.rph.sync.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val pronouns: String? = null,
    val bestIdentifier: String,
    val gameUserProfilePictureUrl: String? = null
)
