package eu.codlab.lorcana.rph

import eu.codlab.lorcana.rph.sync.phases.TournamentPhase
import eu.codlab.lorcana.rph.sync.standings.UserEventStatus
import kotlinx.serialization.Serializable

@Serializable
data class UserEventStatusHolder(
    val status: UserEventStatus
)