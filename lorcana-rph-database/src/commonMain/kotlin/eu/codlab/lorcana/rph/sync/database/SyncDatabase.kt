package eu.codlab.lorcana.rph.sync.database

import eu.codlab.lorcana.rph.sync.event.EventController
import eu.codlab.lorcana.rph.sync.event.EventControllerImpl
import eu.codlab.lorcana.rph.sync.event.EventSettingsController
import eu.codlab.lorcana.rph.sync.event.EventSettingsControllerImpl
import eu.codlab.lorcana.rph.sync.gameplay.GameplayFormatController
import eu.codlab.lorcana.rph.sync.gameplay.GameplayFormatControllerImpl
import eu.codlab.lorcana.rph.sync.match.EventMatchController
import eu.codlab.lorcana.rph.sync.match.EventMatchControllerImpl
import eu.codlab.lorcana.rph.sync.phases.TournamentPhaseController
import eu.codlab.lorcana.rph.sync.phases.TournamentPhaseControllerImpl
import eu.codlab.lorcana.rph.sync.round.RoundController
import eu.codlab.lorcana.rph.sync.round.RoundControllerImpl
import eu.codlab.lorcana.rph.sync.settings.SettingController
import eu.codlab.lorcana.rph.sync.settings.SettingControllerImpl
import eu.codlab.lorcana.rph.sync.standings.EventStandingController
import eu.codlab.lorcana.rph.sync.standings.EventStandingControllerImpl
import eu.codlab.lorcana.rph.sync.standings.UserEventStatusController
import eu.codlab.lorcana.rph.sync.standings.UserEventStatusControllerImpl
import eu.codlab.lorcana.rph.sync.store.StoreController
import eu.codlab.lorcana.rph.sync.store.StoreControllerImpl
import eu.codlab.lorcana.rph.sync.user.UserController
import eu.codlab.lorcana.rph.sync.user.UserControllerImpl

object SyncDatabase {
    private val database: AppDatabase = getDatabase()
    val stores: StoreController = StoreControllerImpl(database)
    val users: UserController = UserControllerImpl(database)
    val gameplayFormats: GameplayFormatController = GameplayFormatControllerImpl(database)
    val eventSettings: EventSettingsController = EventSettingsControllerImpl(database)
    val events: EventController = EventControllerImpl(database)
    val rounds: RoundController = RoundControllerImpl(database)
    val tournamentPhases: TournamentPhaseController = TournamentPhaseControllerImpl(database)
    val eventStandings: EventStandingController = EventStandingControllerImpl(database)
    val userEventStatus: UserEventStatusController = UserEventStatusControllerImpl(database)
    val eventMatches: EventMatchController = EventMatchControllerImpl(database)
    val settings: SettingController = SettingControllerImpl(database)
    // val registeredApps: RegisteredAppController = RegisteredAppControllerImpl(database)
}
