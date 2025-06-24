package eu.codlab.lorcana.rph.rounds.standings

import eu.codlab.lorcana.rph.rounds.matches.EventMatchPlayer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserEventStatus(
    val id: Int,
    @SerialName("matches_won")
    val matchesWon: Int,
    @SerialName("matches_drawn")
    val matchesDrawn: Int,
    @SerialName("matches_lost")
    val matchesLost: Int,
    @SerialName("total_match_points")
    val totalMatchPoints: Int,
    @SerialName("registration_status")
    val registrationStatus: String,
    @SerialName("full_profile_picture_url")
    val fullProfilePictureUrl: String? = null,
    @SerialName("best_identifier")
    val bestIdentifier: String,
    val user: EventMatchPlayer? = null
)
