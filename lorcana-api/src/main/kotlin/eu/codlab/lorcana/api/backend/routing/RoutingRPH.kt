package eu.codlab.lorcana.api.backend.routing

import eu.codlab.lorcana.api.environment.Environment
import eu.codlab.lorcana.rph.EventHolderFull
import eu.codlab.lorcana.rph.StoreHolder
import eu.codlab.lorcana.rph.event.Event
import eu.codlab.lorcana.rph.event.EventQueryParameters
import eu.codlab.lorcana.rph.event.Round
import eu.codlab.lorcana.rph.event.TournamentPhase
import eu.codlab.lorcana.rph.registrations.EventRegistrationsQueryParameters
import eu.codlab.lorcana.rph.rounds.matches.EventMatch
import eu.codlab.lorcana.rph.rounds.standings.EventStanding
import eu.codlab.lorcana.rph.rounds.standings.UserEventStatus
import eu.codlab.lorcana.rph.sync.store.Store
import eu.codlab.lorcana.rph.sync.user.User
import io.bkbn.kompendium.core.metadata.GetInfo
import io.bkbn.kompendium.core.plugin.NotarizedRoute
import io.bkbn.kompendium.json.schema.definition.TypeDefinition
import io.bkbn.kompendium.oas.common.ExternalDocumentation
import io.bkbn.kompendium.oas.payload.Parameter
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.net.URI

fun Route.routeRPH(environment: Environment) {
    val ravensburger = environment.ravenburgerController

    route("/users") {
        install(NotarizedRoute()) {
            get = GetInfo.builder {
                summary("Retrieve the list of users")
                description("Will give you all the users from the platform")

                parameters(
                    Parameter(
                        name = "matching",
                        required = false,
                        schema = TypeDefinition.LONG,
                        `in` = Parameter.Location.query,
                    )
                )
                externalDocumentation(
                    ExternalDocumentation(
                        URI(environment.urlDocumentation),
                        "Get help on Discord"
                    )
                )
                response {
                    responseCode(HttpStatusCode.OK)
                    responseType<List<User>>()
                    description("List of users")
                }
            }
        }

        get {
            try {
                call.respond(ravensburger.users(call.queryParameters["matching"]))
            } catch (err: Throwable) {
                err.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }

    route("/stores") {
        install(NotarizedRoute()) {
            get = GetInfo.builder {
                summary("Retrieve the list of stores")
                description("Will give you all the stores from the platform")

                externalDocumentation(
                    ExternalDocumentation(
                        URI(environment.urlDocumentation),
                        "Get help on Discord"
                    )
                )
                response {
                    responseCode(HttpStatusCode.OK)
                    responseType<List<StoreHolder>>()
                    description("List of stores")
                }
            }
        }

        get {
            try {
                call.respond(ravensburger.stores())
            } catch (err: Throwable) {
                err.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }

    route("/events") {
        install(NotarizedRoute()) {
            get = GetInfo.builder {
                summary("Retrieve the list of events")
                description("Will give you all the events from the platform")

                parameters(
                    Parameter(
                        name = "startingAt",
                        required = false,
                        schema = TypeDefinition.LONG,
                        `in` = Parameter.Location.query,
                        description = "When set, must be accompanied by startingLast"
                    ),
                    Parameter(
                        name = "startingLast",
                        required = false,
                        schema = TypeDefinition.LONG,
                        `in` = Parameter.Location.query,
                        description = "When set, must be accompanied by startingAt"
                    )
                )
                externalDocumentation(
                    ExternalDocumentation(
                        URI(environment.urlDocumentation),
                        "Get help on Discord"
                    )
                )
                response {
                    responseCode(HttpStatusCode.OK)
                    responseType<List<Event>>()
                    description("List of events")
                }
            }
        }

        get {
            try {
                val events = ravensburger.events()

                fun param(key: String): Long? = call.queryParameters[key]?.toLongOrNull()

                fun filterStart(start: Long, end: Long) = events.filter { holder ->
                    holder.event.startDatetime?.let { it in start..end } ?: false
                }

                val interval = (param("startingAt") to param("startingLast"))

                call.respond(
                    interval.takeIf { it.first != null && it.second != null }?.let {
                        filterStart(interval.first!!, interval.second!!)
                    } ?: events
                )
            } catch (err: Throwable) {
                err.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }

    route("/store/{storeId}/events") {
        install(NotarizedRoute()) {
            get = GetInfo.builder {
                summary("Get all the events from a specific store")
                description("Will give you all the info about an event whatever its status")
                externalDocumentation(
                    ExternalDocumentation(
                        URI(environment.urlDocumentation),
                        "Get help on Discord"
                    )
                )
                response {
                    responseCode(HttpStatusCode.OK)
                    responseType<EventHolderFull>()
                    description("Get all information about an event")
                }
            }
        }

        get {
            try {
                val storeId = call.parameters["storeId"]!!.toLong()
                val actualEvent = ravensburger.eventsForStore(storeId)

                call.respond(actualEvent)
            } catch (err: Throwable) {
                err.printStackTrace()
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }

    route("/user/{userId}/events") {
        install(NotarizedRoute()) {
            get = GetInfo.builder {
                summary("Get all the events where a specific user belongs")
                description("Will give you all the info about an event whatever its status")
                externalDocumentation(
                    ExternalDocumentation(
                        URI(environment.urlDocumentation),
                        "Get help on Discord"
                    )
                )
                response {
                    responseCode(HttpStatusCode.OK)
                    responseType<EventHolderFull>()
                    description("Get all information about an event")
                }
            }
        }

        get {
            try {
                val userId = call.parameters["userId"]!!.toLong()
                val actualEvent = ravensburger.eventsForUser(userId)

                call.respond(actualEvent)
            } catch (err: Throwable) {
                err.printStackTrace()
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }

    route("/event/{eventId}") {
        install(NotarizedRoute()) {
            get = GetInfo.builder {
                summary("Retrieve the information about an event")
                description("Will give you all the info about an event whatever its status")
                externalDocumentation(
                    ExternalDocumentation(
                        URI(environment.urlDocumentation),
                        "Get help on Discord"
                    )
                )
                response {
                    responseCode(HttpStatusCode.OK)
                    responseType<EventHolderFull>()
                    description("Get all information about an event")
                }
            }
        }

        get {
            try {
                val eventId = call.parameters["eventId"]!!.toLong()
                val actualEvent = ravensburger.event(eventId)

                call.respond(actualEvent)
            } catch (err: Throwable) {
                err.printStackTrace()
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }

    route("/raw/events") {
        install(NotarizedRoute()) {
            get = GetInfo.builder {
                summary("Retrieve the list of events")
                description("Will give you all the events from the platform")
                externalDocumentation(
                    ExternalDocumentation(
                        URI(environment.urlDocumentation),
                        "Get help on Discord"
                    )
                )
                response {
                    responseCode(HttpStatusCode.OK)
                    responseType<List<Event>>()
                    description("List of events")
                }
            }
        }

        get {
            val events =
                ravensburger.directApiAccess().events(EventQueryParameters(pageSize = 30000))
            call.respond(events)
        }
    }

    route("/raw/event/{eventId}") {
        install(NotarizedRoute()) {
            get = GetInfo.builder {
                summary("Retrieve the information about an event")
                description("Will give you all the info about an event whatever its status")
                externalDocumentation(
                    ExternalDocumentation(
                        URI(environment.urlDocumentation),
                        "Get help on Discord"
                    )
                )
                response {
                    responseCode(HttpStatusCode.OK)
                    responseType<EventExtended>()
                    description("Get all information about an event")
                }
            }
        }

        get {
            try {
                val eventId = call.parameters["eventId"]!!.toLong()
                val actualEvent = ravensburger.directApiAccess().event(eventId)

                val registrations = ravensburger.directApiAccess().eventRegistrations(
                    eventId,
                    EventRegistrationsQueryParameters(pageSize = 3000)
                )

                val tournamentPhases = actualEvent.tournamentPhases.map { tournamentPhase ->
                    val rounds = tournamentPhase.rounds.map { round ->
                        println("   -> managing round ${round.id}")
                        val standings = ravensburger.directApiAccess().eventRoundsStandings(
                            round.id,
                            EventRegistrationsQueryParameters(pageSize = 3000)
                        )
                        val matches = ravensburger.directApiAccess().eventRoundsMatches(
                            round.id,
                            EventRegistrationsQueryParameters(pageSize = 3000)
                        )
                        println("   |-> standings / ${standings.results.size}")
                        println("   |-> matches   / ${matches.results.size}")

                        ExtendedRound(
                            round = round,
                            standings = standings.results,
                            matches = matches.results
                        )
                    }

                    ExtendedTournamentPhase(
                        tournamentPhase = tournamentPhase,
                        rounds = rounds
                    )
                }

                call.respond(
                    EventExtended(
                        event = actualEvent,
                        registrations = registrations.results,
                        tournamentPhases = tournamentPhases
                    )
                )
            } catch (err: Throwable) {
                err.printStackTrace()
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}

@Serializable
data class EventExtended(
    val event: Event,
    val registrations: List<UserEventStatus>,
    @SerialName("tournament_phases")
    val tournamentPhases: List<ExtendedTournamentPhase>
)

@Serializable
data class ExtendedTournamentPhase(
    @SerialName("tournament_phases")
    val tournamentPhase: TournamentPhase,
    val rounds: List<ExtendedRound>
)

@Serializable
data class ExtendedRound(
    val round: Round,
    val standings: List<EventStanding>,
    val matches: List<EventMatch>
)
