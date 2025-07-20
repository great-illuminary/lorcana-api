package eu.codlab.lorcana.api.backend.routing

import eu.codlab.lorcana.api.environment.Environment
import eu.codlab.lorcana.api.environment.discord
import eu.codlab.lorcana.api.utils.installWithOpenApiCategory
import eu.codlab.lorcana.api.utils.trySentry
import eu.codlab.lorcana.rph.EventHolderFull
import eu.codlab.lorcana.rph.StoreHolder
import eu.codlab.lorcana.rph.event.Event
import eu.codlab.lorcana.rph.event.Round
import eu.codlab.lorcana.rph.event.TournamentPhase
import eu.codlab.lorcana.rph.rounds.matches.EventMatch
import eu.codlab.lorcana.rph.rounds.standings.EventStanding
import eu.codlab.lorcana.rph.rounds.standings.UserEventStatus
import eu.codlab.lorcana.rph.sync.user.User
import io.bkbn.kompendium.core.metadata.GetInfo
import io.bkbn.kompendium.core.plugin.NotarizedRoute
import io.bkbn.kompendium.json.schema.definition.TypeDefinition
import io.bkbn.kompendium.oas.payload.Parameter
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Suppress("LongMethod")
fun Route.routeRPH(environment: Environment) {
    val ravensburger = environment.ravensburgerController

    fun Route.overrideInstall(body: NotarizedRoute.Config.() -> Unit) =
        installWithOpenApiCategory("Ravensburger's PlayHub", body)

    route("/users") {
        overrideInstall {
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

                discord(environment)

                response {
                    responseCode(HttpStatusCode.OK)
                    responseType<List<User>>()
                    description("List of users")
                }
            }
        }

        get {
            trySentry(
                onError = { call.respond(HttpStatusCode.InternalServerError) }
            ) {
                call.respond(ravensburger.users(call.queryParameters["matching"]))
            }
        }
    }

    route("/stores") {
        overrideInstall {
            get = GetInfo.builder {
                summary("Retrieve the list of stores")
                description("Will give you all the stores from the platform")

                discord(environment)

                response {
                    responseCode(HttpStatusCode.OK)
                    responseType<List<StoreHolder>>()
                    description("List of stores")
                }
            }
        }

        get {
            trySentry(
                onError = { call.respond(HttpStatusCode.InternalServerError) }
            ) {
                call.respond(ravensburger.stores())
            }
        }
    }

    route("/events") {
        overrideInstall {
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

                discord(environment)

                response {
                    responseCode(HttpStatusCode.OK)
                    responseType<List<Event>>()
                    description("List of events")
                }
            }
        }

        get {
            trySentry(
                onError = { call.respond(HttpStatusCode.InternalServerError) }
            ) {
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
            }
        }
    }

    route("/store/{storeId}/events") {
        overrideInstall {
            get = GetInfo.builder {
                summary("Get all the events from a specific store")
                description("Retrieve the lists of events for a given store")

                parameters(
                    Parameter(
                        name = "storeId",
                        required = true,
                        schema = TypeDefinition.INT,
                        `in` = Parameter.Location.path
                    )
                )

                discord(environment)

                response {
                    responseCode(HttpStatusCode.OK)
                    responseType<EventHolderFull>()
                    description("Get all information about an event")
                }
            }
        }

        get {
            trySentry(
                onError = { call.respond(HttpStatusCode.NotFound) }
            ) {
                val storeId = call.parameters["storeId"]!!.toLong()
                val actualEvent = ravensburger.eventsForStore(storeId)

                call.respond(actualEvent)
            }
        }
    }

    route("/user/{userId}/events") {
        overrideInstall {
            get = GetInfo.builder {
                summary("Get all the events where a specific user belongs")
                description("Will give you all the info about an event whatever its status")

                discord(environment)

                response {
                    responseCode(HttpStatusCode.OK)
                    responseType<EventHolderFull>()
                    description("Get all information about an event")
                }
            }
        }

        get {
            trySentry(
                onError = { call.respond(HttpStatusCode.NotFound) }
            ) {
                val userId = call.parameters["userId"]!!.toLong()
                val actualEvent = ravensburger.eventsForUser(userId)

                call.respond(actualEvent)
            }
        }
    }

    route("/event/{eventId}") {
        overrideInstall {
            get = GetInfo.builder {
                summary("Retrieve the information about an event")
                description("Will give you all the info about an event whatever its status")

                discord(environment)

                response {
                    responseCode(HttpStatusCode.OK)
                    responseType<EventHolderFull>()
                    description("Get all information about an event")
                }
            }
        }

        get {
            trySentry(
                onError = { call.respond(HttpStatusCode.NotFound) }
            ) {
                val eventId = call.parameters["eventId"]!!.toLong()
                val actualEvent = ravensburger.event(eventId)

                call.respond(actualEvent)
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
