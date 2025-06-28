package eu.codlab.lorcana.rph

import eu.codlab.lorcana.rph.sync.phases.TournamentPhase
import eu.codlab.lorcana.rph.sync.round.Round
import eu.codlab.lorcana.rph.sync.standings.EventStanding
import eu.codlab.lorcana.rph.sync.standings.UserEventStatus
import kotlinx.serialization.Serializable

@Serializable
data class EventStandingHolder(
    val standing: EventStanding
)