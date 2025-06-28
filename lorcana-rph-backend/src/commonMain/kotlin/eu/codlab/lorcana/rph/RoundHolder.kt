package eu.codlab.lorcana.rph

import eu.codlab.lorcana.rph.sync.round.Round
import eu.codlab.lorcana.rph.sync.standings.EventStanding
import kotlinx.serialization.Serializable

@Serializable
data class RoundHolder(
    val round: Round,
    val standings: List<EventStanding>
)