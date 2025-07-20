package eu.codlab.lorcana.api.backend.routing

import eu.codlab.lorcana.api.environment.Environment
import eu.codlab.lorcana.api.environment.discord
import eu.codlab.lorcana.api.utils.installWithOpenApiCategory
import eu.codlab.lorcana.api.utils.trySentry
import eu.codlab.lorcana.rph.event.Event
import eu.codlab.lorcana.rph.event.EventQueryParameters
import eu.codlab.lorcana.rph.registrations.EventRegistrationsQueryParameters
import io.bkbn.kompendium.core.metadata.GetInfo
import io.bkbn.kompendium.core.plugin.NotarizedRoute
import io.bkbn.kompendium.json.schema.definition.TypeDefinition
import io.bkbn.kompendium.oas.payload.Parameter
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

@Suppress("LongMethod")
fun Route.routeRPHRaw(environment: Environment) {
    val ravensburger = environment.ravensburgerController

    fun Route.overrideInstall(body: NotarizedRoute.Config.() -> Unit) =
        installWithOpenApiCategory("Ravensburger's PlayHub Raw", body)

    route("/raw/events") {
        overrideInstall {
            get = GetInfo.builder {
                summary("Retrieve the list of events")
                description("Will give you all the events from the platform")

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
                onError = { call.respond(HttpStatusCode.NotFound) }
            ) {
                val events =
                    ravensburger.directApiAccess().events(EventQueryParameters(pageSize = 30000))
                call.respond(events)
            }
        }
    }

    route("/raw/event/{eventId}") {
        overrideInstall {
            get = GetInfo.builder {
                summary("Retrieve the information about an event")
                description("Will give you all the info about an event whatever its status")

                parameters(
                    Parameter(
                        name = "eventId",
                        required = true,
                        schema = TypeDefinition.INT,
                        `in` = Parameter.Location.path
                    )
                )

                discord(environment)

                response {
                    responseCode(HttpStatusCode.OK)
                    responseType<EventExtended>()
                    description("Get all information about an event")
                }
            }
        }

        get {
            trySentry(
                onError = { call.respond(HttpStatusCode.NotFound) }
            ) {
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
            }
        }
    }
}
