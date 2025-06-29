package eu.codlab.lorcana.rph.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class DisplayStatus {
    @SerialName("inProgress")
    InProgress,

    @SerialName("complete")
    Complete,

    @SerialName("past")
    Past,

    @SerialName("upcoming")
    Upcoming,
}
