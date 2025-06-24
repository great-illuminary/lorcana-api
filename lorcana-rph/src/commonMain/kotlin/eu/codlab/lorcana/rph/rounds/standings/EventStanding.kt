package eu.codlab.lorcana.rph.rounds.standings

import eu.codlab.lorcana.rph.user.UserCondensed
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventStanding(
    val rank: Int,
    val player: UserCondensed,
    @SerialName("user_event_status")
    val userEventStatus: UserEventStatus? = null,
    val record: String,
    @SerialName("match_record")
    val matchRecord: String,
    @SerialName("match_points")
    val matchPoints: Int,
    @SerialName("opponent_match_win_percentage")
    val opponentMatchWinPercentage: Double,
    @SerialName("opponent_game_win_percentage")
    val opponentGameWinPercentage: Double
)
