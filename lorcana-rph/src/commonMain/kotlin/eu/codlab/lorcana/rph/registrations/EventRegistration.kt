package eu.codlab.lorcana.rph.registrations

import eu.codlab.lorcana.rph.user.UserCondensed
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventRegistration(
    val id: Int,
    val user: UserCondensed,
    @SerialName("registration_status")
    val registrationStatus: String,
    @SerialName("special_user_identifier")
    val specialUserIdentifier: String? = null,
    @SerialName("best_identifier")
    val bestIdentifier: String,
    @SerialName("matches_won")
    val matchesWon: Int,
    @SerialName("matches_lost")
    val matchesLost: Int,
    @SerialName("matches_drawn")
    val matchesDrawn: Int,
    @SerialName("total_match_points")
    val totalMatchPoints: Int,
    @SerialName("final_place_in_standings")
    val finalPlaceInStandings: Int? = null,
    @SerialName("registration_completed_datetime")
    val registrationCompletedDatetime: String,
    @SerialName("full_profile_picture_url")
    val fullProfilePictureUrl: String? = null,
)