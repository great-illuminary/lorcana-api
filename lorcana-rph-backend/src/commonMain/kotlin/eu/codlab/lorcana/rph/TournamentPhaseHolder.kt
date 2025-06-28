package eu.codlab.lorcana.rph

import eu.codlab.lorcana.rph.sync.phases.TournamentPhase
import kotlinx.serialization.Serializable

@Serializable
data class TournamentPhaseHolder(
    val phase: TournamentPhase,
    val rounds: List<RoundHolder>
)