package eu.codlab.lorcana.rph

import eu.codlab.lorcana.rph.sync.Sync

class RavensburgerController {
    private val loader = LoadRPHCall()
    private val synchronizer = Sync()

    fun event(id: Long): EventHolderFull {
        val event = synchronizer.eventAccess.getFromId(id)!!

        return EventHolderFull(
            event = event,
            settings = synchronizer.settingsAccess.getFromId(event.settingsId)!!,
            registrations = synchronizer.userEventStatusAccess.getFromParent(event.id)
                .map { status ->
                    UserEventStatusHolder(
                        player = synchronizer.userAccess.getFromId(status.userId)!!,
                        status = status
                    )
                },
            tournamentPhases = synchronizer.tournamentPhaseAccess.getFromParent(event.id)
                .map { phase ->
                    TournamentPhaseHolder(
                        phase = phase,
                        rounds = synchronizer.roundAccess.getFromParent(phase.id).map { round ->
                            RoundHolder(
                                round = round,
                                standings = synchronizer.eventStandingAccess.getFromParent(round.id),
                                matches = synchronizer.eventMatchAccess.getFromParent(round.id)
                            )
                        }
                    )
                },
            gameplayFormat = synchronizer.gameplayFormatAccess.getFromId(event.gameplayFormatId),
            store = event.storeId?.let { synchronizer.storeAccess.getFromId(it) }
        )
    }

    fun stores() = synchronizer.storeAccess.getCachedList().map {
        StoreHolder(
            id = it.id,
            name = it.name,
            fullAddress = it.fullAddress,
            administrativeAreaLevel1Short = it.administrativeAreaLevel1Short,
            country = it.country,
            website = it.website,
            latitude = it.latitude,
            longitude = it.longitude,
            email = it.email,
            streetAddress = it.streetAddress,
            zipcode = it.zipcode,
            phoneNumber = it.phoneNumber,
            storeTypes = it.storeTypesList,
            storeTypesPretty = it.storeTypesPrettyList
        )
    }

    fun events(): List<EventHolder> {
        val events = synchronizer.eventAccess.getCachedList()

        return events.map {
            EventHolder(
                event = it,
                settings = synchronizer.settingsAccess.getFromId(it.settingsId)!!,
                registrations = synchronizer.userEventStatusAccess.getFromParent(it.id)
                    .map { status ->
                        UserEventStatusHolder(
                            player = synchronizer.userAccess.getFromId(status.userId),
                            status = status
                        )
                    },
                tournamentPhases = synchronizer.tournamentPhaseAccess.getFromParent(it.id),
                gameplayFormat = synchronizer.gameplayFormatAccess.getFromId(it.gameplayFormatId),
                store = it.storeId?.let { synchronizer.storeAccess.getFromId(it) }
            )
        }
    }

    suspend fun eventsForUser(userId: Long): List<EventHolderFull> {
        val user = user(userId) ?: throw IllegalStateException("Not found")
        val events = synchronizer.eventAccess.forUserIdsOnly(user)

        return events.map { event(it) }
    }

    suspend fun eventsForStore(storeId: Long): List<EventHolderFull> {
        val store = store(storeId) ?: throw IllegalStateException("Not found")
        val events = synchronizer.eventAccess.forStoreIdsOnly(store)

        return events.map { event(it) }
    }

    fun directApiAccess() = loader

    suspend fun startSync() {
        synchronizer.start()
    }

    private fun user(id: Long) = synchronizer.userAccess.getFromId(id)

    private fun store(id: Long) = synchronizer.storeAccess.getFromId(id)

    suspend fun users(matching: String? = null) = if (null != matching) {
        synchronizer.userAccess.matching(matching)
    } else {
        synchronizer.userAccess.getCachedList()
    }
}
