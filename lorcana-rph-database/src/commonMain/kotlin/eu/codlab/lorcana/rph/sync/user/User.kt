package eu.codlab.lorcana.rph.sync.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import eu.codlab.lorcana.rph.sync.ModelId
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val pronouns: String? = null,
    val bestIdentifier: String? = null,
    /**
     * Obtained inside UserEventStatus directly
     */
    val bestIdentifierInGame: String? = null,
    @Transient
    val gameUserProfilePictureUrl: String? = null
) : ModelId<Long> {
    override fun modelId() = id
}
