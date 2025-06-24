package eu.codlab.lorcana.rph.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class EventFormat {
    @SerialName("OTHER")
    Other
}