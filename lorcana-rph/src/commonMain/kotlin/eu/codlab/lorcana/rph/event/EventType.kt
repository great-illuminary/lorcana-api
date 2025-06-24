package eu.codlab.lorcana.rph.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class EventType {
    @SerialName("LOCALS")
    Locals
}