package eu.codlab.lorcana.rph

import eu.codlab.lorcana.rph.event.Event
import eu.codlab.lorcana.rph.event.EventQueryParameters
import eu.codlab.lorcana.rph.registrations.EventRegistrationsQueryParameters
import eu.codlab.lorcana.rph.rounds.matches.EventMatch
import eu.codlab.lorcana.rph.rounds.standings.EventStanding
import eu.codlab.lorcana.rph.rounds.standings.UserEventStatus
import eu.codlab.lorcana.rph.utils.Clients
import eu.codlab.lorcana.rph.utils.Page

class LoadRPHCall {
    private val workersDev =
        "https://cf-worker-middleware-hydra-prod.devion-villegas-76b.workers.dev/hydraproxy"
    private val rph = "https://api.ravensburgerplay.com/api/v2"

    suspend fun events(parameters: EventQueryParameters = EventQueryParameters()) =
        Clients.get<Page<Event>>("$workersDev/api/v2/events/?${parameters.toUrl}")

    suspend fun event(
        id: Long,
    ) = Clients.get<Event>("$rph/events/$id/")

    suspend fun eventRegistrations(
        id: Long,
        parameters: EventRegistrationsQueryParameters = EventRegistrationsQueryParameters()
    ) = Clients.get<Page<UserEventStatus>>("$rph/events/$id/registrations/?${parameters.toUrl}")

    suspend fun eventRoundsStandings(
        id: Long,
        parameters: EventRegistrationsQueryParameters = EventRegistrationsQueryParameters()
    ) =
        Clients.get<Page<EventStanding>>("$rph/tournament-rounds/$id/standings/paginated/?${parameters.toUrl}")

    suspend fun eventRoundsMatches(
        id: Long,
        parameters: EventRegistrationsQueryParameters = EventRegistrationsQueryParameters()
    ) =
        Clients.get<Page<EventMatch>>("$rph/tournament-rounds/$id/matches/paginated/?${parameters.toUrl}")
}
