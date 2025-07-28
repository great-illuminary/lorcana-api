package eu.codlab.lorcana.rph

import eu.codlab.lorcana.rph.sync.Sync

class RavensburgerController(
    onError: (Throwable) -> Unit
) {
    /**
     * Access the Play Hub's API directly.
     *
     * Notice : if issues arise in the future with the frontend, it will be possible
     * to load those through the proxy as a fallback via
     *
     * // inside the controller
     * val clientWithProxy = createClient(
     *   Configuration(
     *     json = json,
     *     enableLogs = true,
     *     socketTimeoutMillis = 30000,
     *     connectTimeoutMillis = 30000,
     *     requestTimeoutMillis = 30000,
     *     proxyConfiguration = ProxyConfiguration(
     *       proxyConfig = ProxyBuilder.http(Url("http://api.zenrows.com:8001")),
     *       proxyAuthentication = {
     *         Config.zenrows to "js_render=true&premium_proxy=true&proxy_country=fr"
     *       },
     *       trustCertificatesOnAndroidJvm = true
     *     ),
     *   )
     * ) { }
     *
     * private val loader = LoadRPHCall { url ->
     *   clientWithProxy.get(url) {
     *     val clientInfo =
     *       "${Config.zenrows}:js_render=true&premium_proxy=true&proxy_country=fr"
     *     val basicAuth = Base64.getEncoder().encodeToString(clientInfo.toByteArray())
     *     header(HttpHeaders.ProxyAuthorization, "Basic $basicAuth")
     *   }
     * }
     */
    private val loader = LoadRPHCall()
    private val synchronizer = Sync(onError)

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
            store = event.storeId?.let { synchronizer.storeAccess.getFromId(it)?.toStoreHolder() }
        )
    }

    fun stores() = synchronizer.storeAccess.getCachedList().map { it.toStoreHolder() }

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
                store = it.storeId?.let { s ->
                    synchronizer.storeAccess.getFromId(s)?.toStoreHolder()
                }
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
