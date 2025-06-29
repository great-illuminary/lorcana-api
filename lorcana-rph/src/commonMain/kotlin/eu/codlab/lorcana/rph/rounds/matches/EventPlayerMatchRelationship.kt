package eu.codlab.lorcana.rph.rounds.matches

import eu.codlab.lorcana.rph.rounds.standings.UserEventStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventPlayerMatchRelationship(
    @SerialName("player_order")
    val playerOrder: Int? = null,
    val player: EventMatchPlayer,
    @SerialName("user_event_status")
    val userEventStatus: UserEventStatus,
)
