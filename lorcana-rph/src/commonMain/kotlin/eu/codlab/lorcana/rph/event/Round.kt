package eu.codlab.lorcana.rph.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Round(
    val id: Long,
    @SerialName("round_number")
    val roundNumber: Int,
    @SerialName("final_round_in_event")
    val finalRoundInEvent: Boolean,
    @SerialName("pairings_status")
    val pairingsStatus: String,
    @SerialName("standings_status")
    val standingsStatus: String,
    @SerialName("round_type")
    val roundType: String,
    val status: String,
)
