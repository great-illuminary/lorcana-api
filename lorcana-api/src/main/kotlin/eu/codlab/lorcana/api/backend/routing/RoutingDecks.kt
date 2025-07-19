package eu.codlab.lorcana.api.backend.routing

import eu.codlab.lorcana.api.environment.Environment
import eu.codlab.lorcana.api.environment.discord
import eu.codlab.lorcana.dreamborn.database.MappingDeck
import eu.codlab.lorcana.dreamborn.decks.DeckDescriptorLight
import io.bkbn.kompendium.core.metadata.GetInfo
import io.bkbn.kompendium.core.plugin.NotarizedRoute
import io.bkbn.kompendium.oas.common.ExternalDocumentation
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingHandler
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import java.net.URI

fun Route.decks(environment: Environment) {
    fun Route.routeDecks(
        path: String,
        summary: String,
        description: String,
        body: RoutingHandler
    ) = route(path) {
        install(NotarizedRoute()) {
            get = GetInfo.builder {
                summary(summary)
                description(description)

                discord(environment)

                response {
                    responseCode(HttpStatusCode.OK)
                    responseType<List<MappingDeck>>()
                    description("The list of corresponding decks")
                }
            }
        }

        get(body)
    }

    routeDecks(
        "/decks/trending",
        "Retrieve the trending decks",
        "Will return a list of decks"
    ) {
        val decks = environment.dreamborn.decks().filter { (it.lastTrendingAtMs ?: 0) > 0 }
        call.respond(decks)
    }

    routeDecks(
        "/decks",
        "Retrieve all the decks",
        "Will return a list of decks"
    ) {
        val decks = environment.dreamborn.decks().filter { !it.isPrivate }
        call.respond(decks)
    }

    routeDecks(
        "/creator/{creator}/decks",
        "Retrieve all the decks",
        "Will return a list of decks"
    ) {
        val creator = call.parameters["creator"]!!
        val decks = environment.dreamborn.deckFromCreator(creator).filter { !it.isPrivate }
        call.respond(decks)
    }

    route("/deck/fetch/{deck}") {
        get {
            val deckId = call.parameters["deck"]!!
            environment.dreamborn.fetchDeck(deckId).let {
                if (null != it) {
                    call.respond(it)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }

    route("/deck/fetch/light/{deck}") {
        get {
            val deckId = call.parameters["deck"]!!

            try {
                val regular = environment.dreamborn.fetchDeck(deckId)!!

                call.respond(
                    DeckDescriptorLight(
                        id = regular.uuid,
                        cards = regular.cards.associate { it.dreamborn to it.count },
                        url = ""
                    )
                )
            } catch (err: Throwable) {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }

    route("/deck/{deck}") {
        install(NotarizedRoute()) {
            get = GetInfo.builder {
                summary("Retrieve the actual full deck from a given id")
                description("Will give you all data for a given deck")

                discord(environment)

                response {
                    responseCode(HttpStatusCode.OK)
                    responseType<MappingDeck>()
                    description("Deck content")
                }
                canRespond {
                    responseCode(HttpStatusCode.NotFound)
                    responseType<Unit>()
                    description("The given deck couldn't be found")
                }
            }
        }

        get {
            val deckId = call.parameters["deck"]!!
            environment.dreamborn.deck(deckId).let {
                if (null != it) {
                    call.respond(it)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}
