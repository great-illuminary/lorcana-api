package eu.codlab.lorcana.rph.sync

import eu.codlab.lorcana.rph.LoadRPHCall
import eu.codlab.lorcana.rph.event.EventQueryParameters
import eu.codlab.lorcana.rph.registrations.EventRegistrationsQueryParameters
import eu.codlab.lorcana.rph.rounds.standings.EventStanding
import eu.codlab.lorcana.rph.rounds.standings.UserEventStatus
import eu.codlab.lorcana.rph.sync.event.Event
import eu.codlab.lorcana.rph.sync.phases.TournamentPhase
import eu.codlab.lorcana.rph.user.UserCondensed
import kotlinx.serialization.SerialName

class Sync {
    private val loader = LoadRPHCall()

    private val settings = Settings()

    private val eventWrapper = EventWrapper()
    private val storeWrapper = StoreWrapper()
    private val gameplayFormatWrapper = GameplayFormatWrapper()
    private val tournamentPhaseWrapper = TournamentPhaseWrapper()
    private val roundWrapper = RoundWrapper()
    private val settingsWrapper = SettingsWrapper()

    val eventAccess = eventWrapper.asCacheAccess()
    val storeAccess = eventWrapper.asCacheAccess()
    val gameplayFormatAccess = gameplayFormatWrapper.asCacheAccess()
    val tournamentPhaseAccess = tournamentPhaseWrapper.asCacheAccess()
    val roundAccess = roundWrapper.asCacheAccess()
    val settingsAccess = settingsWrapper.asCacheAccess()

    private val wrappers = listOf(
        eventWrapper,
        storeWrapper,
        gameplayFormatWrapper,
        tournamentPhaseWrapper,
        roundWrapper,
        settingsWrapper
    )

    private suspend fun initialize() {
        wrappers.forEach { it.initialize() }

        println("initialization done ${eventAccess.getCachedList().size}")
    }

    suspend fun start() {
        initialize()

        var synchronizationNeedsToContinue = true
        do {
            var lastPage = settings.lastPage()
            println("now performing the synchronization for the page $lastPage")

            val events = loader.events(
                EventQueryParameters(
                    page = lastPage
                )
            )

            events.results.forEach {
                val afterCheck = eventWrapper.check(it)
                val eventFromDatabase: Event = when (afterCheck) {
                    is GeneratedModel -> afterCheck.new
                    is PreviousModel -> afterCheck.previous
                }

                settingsWrapper.check(it.settings)

                //TODO check the settings.event_lifecycle_status
                //TODO check the settings.show_registration_button

                //TODO check that the registered_user_count has changed
                //note : this would be problematic for cases which are :
                // user A joins, user B joins, user A leave
                // resulting in not seeing that it changed but for now, we'll skip this completely

                gameplayFormatWrapper.check(it.gameplayFormat)
                storeWrapper.check(it.store)

                it.tournamentPhases.forEach { phase ->
                    tournamentPhaseWrapper.check(phase, eventFromDatabase).let { checked ->
                        val fromDatabase: TournamentPhase = when (checked) {
                            is GeneratedModel -> checked.new
                            is PreviousModel -> checked.previous
                        }

                        phase.rounds.forEach { round ->
                            val roundChecked = roundWrapper.check(round, fromDatabase)
                            val (previous, next) = when (roundChecked) {
                                is GeneratedModel -> roundChecked.previous to roundChecked.new
                                is PreviousModel -> roundChecked.previous to null
                            }

                            //TODO manage pairings_status has changed from NOT_GENERATED to GENERATED
                            //TODO manage standings_status has changed from NOT_GENERATED to GENERATED
                            val nextIsGenerated = next?.pairingsStatus == "GENERATED"
                            val previousWasntGenerated = previous?.pairingsStatus != "GENERATED"
                            if (null != next && nextIsGenerated && previousWasntGenerated) {
                                val standings: List<EventStanding> = loader.eventRoundsStandings(
                                    next.id, EventRegistrationsQueryParameters(
                                        pageSize = 4000
                                    )
                                ).results

                                standings.firstOrNull()?.userEventStatus
                            }
                        }
                    }
                }
            }

            if (null == events.next) {
                // nothing to do now... we finished
                synchronizationNeedsToContinue = false
            } else {
                lastPage++
                settings.setCurrentPage(lastPage)
            }
        } while (synchronizationNeedsToContinue)
    }
}