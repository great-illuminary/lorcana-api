package eu.codlab.lorcana.rph.sync

import eu.codlab.lorcana.rph.LoadRPHCall
import eu.codlab.lorcana.rph.event.EventQueryParameters
import eu.codlab.lorcana.rph.registrations.EventRegistrationsQueryParameters
import eu.codlab.lorcana.rph.rounds.matches.EventMatch
import eu.codlab.lorcana.rph.rounds.matches.EventMatchPlayer
import eu.codlab.lorcana.rph.rounds.standings.EventStanding
import eu.codlab.lorcana.rph.rounds.standings.UserEventStatus
import eu.codlab.lorcana.rph.store.StoreFullRestLine
import eu.codlab.lorcana.rph.store.StoresQueryParameters
import eu.codlab.lorcana.rph.sync.overrides.UserEventStatusParent
import eu.codlab.lorcana.rph.sync.phases.TournamentPhase
import eu.codlab.lorcana.rph.utils.Page
import korlibs.time.DateTime
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.minutes

class Sync(
    private val onError: (Throwable) -> Unit
) {
    private val loader = LoadRPHCall()

    private val settings = Settings()

    private val eventWrapper = EventWrapper()
    private val storeWrapper = StoreWrapper()
    private val gameplayFormatWrapper = GameplayFormatWrapper()
    private val tournamentPhaseWrapper = TournamentPhaseWrapper()
    private val roundWrapper = RoundWrapper()
    private val settingsWrapper = SettingsWrapper()
    private val eventStandingWrapper = EventStandingWrapper()
    private val userEventStatusWrapper = UserEventStatusWrapper()
    private val eventMatchWrapper = EventMatchWrapper()
    private val userWrapper = UserWrapper()

    val eventAccess: IEventWrapper = eventWrapper
    val storeAccess = storeWrapper.asCacheAccess()
    val gameplayFormatAccess = gameplayFormatWrapper.asCacheAccess()
    val tournamentPhaseAccess = tournamentPhaseWrapper.asCacheAccess()
    val roundAccess = roundWrapper.asCacheAccess()
    val settingsAccess = settingsWrapper.asCacheAccess()
    val eventStandingAccess = eventStandingWrapper.asCacheAccess()
    val userEventStatusAccess = userEventStatusWrapper.asCacheAccess()
    val eventMatchAccess = eventMatchWrapper.asCacheAccess()
    val userAccess: IUserWrapper = userWrapper

    private val wrappers = listOf(
        eventWrapper,
        storeWrapper,
        gameplayFormatWrapper,
        tournamentPhaseWrapper,
        roundWrapper,
        settingsWrapper,
        eventStandingWrapper,
        userEventStatusWrapper,
        eventMatchWrapper,
        userWrapper
    )

    private suspend fun initialize() {
        wrappers.forEach { it.initialize() }

        println("initialization done ${eventAccess.getCachedList().size}")
    }

    suspend fun start() {
        initialize()

        while (true) {
            println("looping the events...")
            tryCatch { loop() }

            delay(10.minutes)
        }
    }

    private suspend fun loop() {
        tryCatch {
            checkUsersToFix()
        }

        tryCatch {
            syncStores()
            fastSyncStore()
        }

        tryCatch {
            syncEvents()
            fastSyncEvent()
        }

        val list = eventAccess.getCachedList()
        println("number of events in -/+2days ${list.filter { it.isIn2DaysOrWas2DaysAgo() }.size}")
        println("number of events in the future ${list.filter { it.isInTheFutureAfter2Days() }.size}")
        println("number of events  from last week ${list.filter { it.fromNthLastWeeks(1) }.size}")
        println("number of events  from 2+ weeks  ${list.filter { it.fromNthLastWeeks(2) }.size}")

        val eventInThePast = eventAccess.getCachedList().filter {
            val settings = settingsAccess.getFromId(it.settingsId)!!

            it.requiresRefresh(settings)
        }

        println("requires refresh for ${eventInThePast.size}")

        eventInThePast.forEach {
            println("need synchronization for ${it.id}")
            tryCatch {
                checkEvent(loader.event(it.id))
            }
            // TODO manage 404 in a new error
            // {"code":"RESP002","detail":"The requested URL page returned a 404 HTTP Status Code. Please make sure this URL exists and retry your request.","instance":"/v1","status":404,"title":"Page not found (RESP002)","type":"https://docs.zenrows.com/api-error-codes#RESP002"}
            // Exception in thread "main" java.lang.IllegalStateException: Couldn't load https://api.ravensburgerplay.com/api/v2/events/163408/
        }
    }

    private suspend fun fastSyncStore() = fastSync(
        PageType.Stores,
        { checkStore(it) },
    ) {
        loader.stores(
            StoresQueryParameters(
                page = it,
                pageSize = 250
            )
        )
    }

    private suspend fun fastSyncEvent() = fastSync(
        PageType.Events,
        {
            tryCatch {
                checkEvent(it, true)
            }
        },
    ) {
        loader.events(
            EventQueryParameters(
                page = it,
                pageSize = 250
            )
        )
    }

    private suspend fun <T> fastSync(
        pageType: PageType = PageType.Events,
        check: suspend (T) -> Unit,
        getObjects: suspend (Int) -> Page<T>
    ) {
        (1..(settings.lastPage(pageType) / 10 + 1)).forEach { lastPage ->
            println("now rechecking quickly the page $lastPage")
            try {
                val events = getObjects(lastPage)

                events.results.forEach { check(it) }
            } catch (err: Throwable) {
                err.printStackTrace()
            }
        }
    }

    private suspend fun syncEvents() =
        performSync(
            pageType = PageType.Events,
            check = { tryCatch { checkEvent(it) } },
        ) { loader.events(EventQueryParameters(page = it)) }

    private suspend fun syncStores() =
        performSync(
            pageType = PageType.Stores,
            check = { tryCatch { checkStore(it) } },
        ) { loader.stores(StoresQueryParameters(page = it)) }

    private suspend fun <T> performSync(
        pageType: PageType,
        check: suspend (T) -> Unit,
        getObjects: suspend (Int) -> Page<T>,
    ) {
        var synchronizationNeedsToContinue = true
        do {
            var lastPage = settings.lastPage(pageType)
            println("now performing the synchronization for the page $lastPage")

            val events = getObjects(lastPage)

            // checking the issue with some players
            events.results.forEach { check(it) }

            if (null == events.next) {
                println("synchronization done")
                // nothing to do now... we finished
                synchronizationNeedsToContinue = false
            } else {
                lastPage++
                settings.setCurrentPage(pageType, lastPage)
            }
        } while (synchronizationNeedsToContinue)
    }

    private suspend fun checkStore(storeFullRestLine: StoreFullRestLine) {
        // skipping the "other" information
        storeWrapper.check(storeFullRestLine.store, storeFullRestLine)
    }

    private suspend fun checkEvent(
        event: eu.codlab.lorcana.rph.event.Event,
        skipEquals: Boolean = false
    ) {
        val afterCheck = eventWrapper.check(event)
        val (previous, new) = when (afterCheck) {
            is GeneratedModel -> afterCheck.previous to afterCheck.new
            is PreviousModel -> afterCheck.previous to null
        }

        if (skipEquals && new == null) {
            println("skipping project")
            return
        }

        val eventFromDatabase = new ?: previous ?: return // throw ?

        val known = userEventStatusAccess.getFromParent(eventFromDatabase.id).filter {
            it.registrationStatus != "DROPPED"
        }

        // note : this would be problematic for cases which are :
        // user A joins, user B joins, user A leave
        // resulting in not seeing that it changed but for now, we'll skip this completely
        if (null != new) {
            // TODO use the registered user list instead

            val hasNewUsers = new.registeredUserCount != previous?.registeredUserCount ||
                    new.registeredUserCount != known.size

            if (hasNewUsers) {
                val registrations: List<UserEventStatus> = loader.eventRegistrations(
                    new.id,
                    EventRegistrationsQueryParameters(pageSize = 40000)
                ).results

                println("  -> differences in the users and registrations is #${registrations.size}")

                registrations.forEach { registration ->
                    println("registration for ${registration.user}")
                    registration.user?.let { nonNullUser ->
                        userEventStatusWrapper.check(
                            registration,
                            UserEventStatusParent(
                                eventId = eventFromDatabase.id,
                                playerId = nonNullUser.id
                            )
                        )

                        userWrapper.check(nonNullUser)
                    }
                }

                // now check the dropped user
                userEventStatusWrapper.getFromParent(eventFromDatabase.id).filter { fromDb ->
                    // we want all the participants which are not in the tournament anymore
                    null == registrations.find { it.id == fromDb.id }
                }.forEach { fromDb ->
                    println("changing the status for the participant ${fromDb.userId} to DROPPED")
                    val dropped = fromDb.copy(registrationStatus = "DROPPED")
                    userEventStatusWrapper.update(dropped)
                }
            }
        }

        val settings = when (val wrapper = settingsWrapper.check(event.settings)) {
            is GeneratedModel -> wrapper.new ?: wrapper.previous
            is PreviousModel -> wrapper.previous
        }

        // TODO check the settings.event_lifecycle_status
        // TODO check the settings.show_registration_button

        gameplayFormatWrapper.check(event.gameplayFormat)
        storeWrapper.check(event.store)

        event.tournamentPhases.forEach { phase ->
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

                    val nextPIsGenerated = next?.pairingsStatus == "GENERATED"
                    val previousPWasnt = previous?.pairingsStatus != "GENERATED"
                    if (null != next && nextPIsGenerated && previousPWasnt) {
                        val pairings: List<EventMatch> = loader.eventRoundsMatches(
                            next.id,
                            EventRegistrationsQueryParameters(pageSize = 4000)
                        ).results

                        pairings.forEach { match ->
                            eventMatchWrapper.check(match)
                            // TODO force recheck the user event status ?
                        }
                    }

                    val nextSIsGenerated = next?.standingsStatus == "GENERATED"
                    val previousSWasnt = previous?.standingsStatus != "GENERATED"

                    if (null != next && nextSIsGenerated && previousSWasnt) {
                        println("managing the event standing for the round ${next.id}")
                        val standings: List<EventStanding> = loader.eventRoundsStandings(
                            next.id,
                            EventRegistrationsQueryParameters(pageSize = 4000)
                        ).results

                        standings.forEach { eventStanding ->
                            val eventStandingChecked =
                                eventStandingWrapper.check(eventStanding, next)
                            val checked = when (eventStandingChecked) {
                                is GeneratedModel -> eventStandingChecked.new
                                is PreviousModel -> eventStandingChecked.previous
                            }

                            // TODO manage the UserEventStatus
                            eventStanding.userEventStatus?.let { userEventStatus ->
                                val statusChecked = userEventStatusWrapper.check(
                                    userEventStatus,
                                    UserEventStatusParent(
                                        eventId = eventFromDatabase.id,
                                        playerId = checked.playerId
                                    )
                                )

                                val statusCheckedDatabase = when (statusChecked) {
                                    is GeneratedModel -> statusChecked.new
                                    is PreviousModel -> statusChecked.previous
                                }

                                val player =
                                    userAccess.getFromId(statusCheckedDatabase.userId)

                                // to "fix" an issue where the identifiers are not the same for
                                // user info and their info in game, we are actually for now
                                // fixing this by forcing a specific set of info

                                val eventMatchPlayer =
                                    player?.toEventMatchPlayer() ?: EventMatchPlayer(
                                        id = statusCheckedDatabase.userId,
                                        bestIdentifier = "",
                                        gameUserProfilePictureUrl = statusCheckedDatabase.fullProfilePictureUrl
                                    )

                                userWrapper.check(eventMatchPlayer, statusCheckedDatabase)
                            }
                        }
                    }
                }
            }
        }

        eventFromDatabase.shouldCommitFinalUpdate(settings!!).let { commit ->
            println("event ${event.id} should be closed ? $commit // ${settings.eventLifecycleStatus}")
            // now commit the event
            eventWrapper.setInternalDataPostTreatment(
                eventFromDatabase,
                commit,
                DateTime.nowUnixMillisLong()
            )
        }
    }

    private suspend fun checkUsersToFix() = userWrapper.fixUsersWithoutProperIdentifier()

    private suspend fun tryCatch(body: suspend () -> Unit) = try {
        body()
    } catch (err: Throwable) {
        onError(err)
    }
}
