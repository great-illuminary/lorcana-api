package eu.codlab.lorcana.rph.rounds.standings

import eu.codlab.lorcana.rph.rounds.matches.EventMatchPlayer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This class is also the UserRegistration
 */
@Serializable
data class UserEventStatus(
    val id: Long,
    @SerialName("matches_won")
    val matchesWon: Int,
    @SerialName("matches_drawn")
    val matchesDrawn: Int,
    @SerialName("matches_lost")
    val matchesLost: Int,
    @SerialName("total_match_points")
    val totalMatchPoints: Int,
    @SerialName("full_profile_picture_url")
    val fullProfilePictureUrl: String? = null,
    @SerialName("best_identifier")
    val bestIdentifier: String,
    val user: EventMatchPlayer? = null,
    @SerialName("registration_status")
    val registrationStatus: String? = null,
    @SerialName("special_user_identifier")
    val specialUserIdentifier: String? = null,
    @SerialName("final_place_in_standings")
    val finalPlaceInStandings: Int? = null,
    @SerialName("registration_completed_datetime")
    val registrationCompletedDatetime: String? = null,
)
