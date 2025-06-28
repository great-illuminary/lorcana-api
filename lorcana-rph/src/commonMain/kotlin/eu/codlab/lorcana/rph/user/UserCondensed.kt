package eu.codlab.lorcana.rph.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserCondensed(
    val id: Long,
    @SerialName("best_identifier")
    val bestIdentifier: String
)
