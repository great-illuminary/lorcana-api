package eu.codlab.lorcana.rph.sync.standings

import androidx.room.Entity
import androidx.room.PrimaryKey
import eu.codlab.lorcana.rph.sync.ModelId

@Entity
data class UserEventStatus(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val matchesWon: Int,
    val matchesDrawn: Int,
    val matchesLost: Int,
    val totalMatchPoints: Int,
    val registrationStatus: String,
    val fullProfilePictureUrl: String? = null,
    val bestIdentifier: String,

    // foreign keys
    val roundId: Long? = null,
    val userId: Long? = null,
    // val eventStandingId: Long? = null
) : ModelId<Long> {
    override fun modelId() = id
}