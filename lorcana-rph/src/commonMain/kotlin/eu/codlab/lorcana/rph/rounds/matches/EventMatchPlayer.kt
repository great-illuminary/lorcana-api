package eu.codlab.lorcana.rph.rounds.matches

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventMatchPlayer(
    val id: Long,
    val pronouns: String? = null,
    @SerialName("best_identifier")
    val bestIdentifier: String,
    @SerialName("game_user_profile_picture_url")
    val gameUserProfilePictureUrl: String? = null
)
