package eu.codlab.lorcana.rph.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TournamentPhase(
    val id: Long,
    @SerialName("first_round_type")
    val firstRoundType: String? = null,
    val status: String,
    @SerialName("order_in_phases")
    val orderInPhases: Int,
    @SerialName("number_of_rounds")
    val numberOfRounds: Int?,
    @SerialName("round_type")
    val roundType: String,
    @SerialName("rank_required_to_enter_phase")
    val rankRequiredToEnterPhase: Int? = null,
    val rounds: List<Round>
)
