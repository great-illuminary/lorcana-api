package eu.codlab.lorcana.rph.sync.standings

import androidx.room.Entity
import androidx.room.PrimaryKey
import eu.codlab.lorcana.rph.sync.ModelId
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@Entity
data class UserEventStatus(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val matchesWon: Int,
    val matchesDrawn: Int,
    val matchesLost: Int,
    val totalMatchPoints: Int,
    val registrationStatus: String? = null,
    @Transient
    val fullProfilePictureUrl: String? = null,
    // val deckId: Long? = null,
    val bestIdentifier: String,
    // we also set few information which were "registration specific"
    val specialUserIdentifier: String? = null,
    val finalPlaceInStandings: Int? = null,
    val registrationCompletedDatetime: String? = null,
    // foreign keys
    @Transient
    val eventId: Long = 0,
    @Transient
    val userId: Long = 0,
    // val eventStandingId: Long? = null
) : ModelId<Long> {
    override fun modelId() = id
}
