package eu.codlab.lorcana.rph

import eu.codlab.lorcana.rph.sync.standings.UserEventStatus
import eu.codlab.lorcana.rph.sync.user.User
import kotlinx.serialization.Serializable

@Serializable
data class UserEventStatusHolder(
    val player: User? = null,
    val status: UserEventStatus
)
