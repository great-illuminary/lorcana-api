package eu.codlab.lorcana.rph.event

import kotlinx.serialization.Serializable

@Serializable
enum class EventStatus {
    UNLISTED,
    SCHEDULED,
    CANCELED,
    ARCHIVED,
}
