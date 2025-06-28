package eu.codlab.lorcana.rph.sync.standings

import androidx.room.Entity

@Entity
data class UserEventStatus(
    val id: Long,
    val matchesWon: Int,
    val matchesDrawn: Int,
    val matchesLost: Int,
    val totalMatchPoints: Int,
    val registrationStatus: String,
    val fullProfilePictureUrl: String? = null,
    val bestIdentifier: String,
    val userId: Long? = null
)