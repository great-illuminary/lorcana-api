package eu.codlab.lorcana.rph.gameplay

import kotlinx.serialization.Serializable

@Serializable
data class GameplayFormat(
    val id: String,
    val name: String,
    /**
     * Key only available in the specific event() endpoint, not the list of events
     */
    val description: String? = null
)
