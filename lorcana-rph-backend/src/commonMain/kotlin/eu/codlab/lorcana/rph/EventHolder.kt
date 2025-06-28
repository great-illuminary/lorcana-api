package eu.codlab.lorcana.rph

import eu.codlab.lorcana.rph.sync.event.Event
import eu.codlab.lorcana.rph.sync.event.EventSettings
import eu.codlab.lorcana.rph.sync.gameplay.GameplayFormat
import eu.codlab.lorcana.rph.sync.phases.TournamentPhase
import kotlinx.serialization.Serializable

@Serializable
data class EventHolder(
    val event: Event,
    val settings: EventSettings,
    val tournamentPhases: List<TournamentPhase>,
    val gameplayFormat: GameplayFormat? = null,
)