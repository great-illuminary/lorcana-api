package eu.codlab.lorcana.api.backend

import eu.codlab.lorcana.api.backend.routing.MappingVirtualCard
import eu.codlab.lorcana.api.backend.routing.artists
import eu.codlab.lorcana.api.backend.routing.decks
import eu.codlab.lorcana.api.backend.routing.routeMap
import eu.codlab.lorcana.api.backend.routing.routeRPH
import eu.codlab.lorcana.api.environment.Environment
import eu.codlab.lorcana.raw.SetDescription
import io.bkbn.kompendium.core.metadata.GetInfo
import io.bkbn.kompendium.core.plugin.NotarizedRoute
import io.bkbn.kompendium.core.routes.swagger
import io.bkbn.kompendium.json.schema.definition.TypeDefinition
import io.bkbn.kompendium.oas.common.ExternalDocumentation
import io.bkbn.kompendium.oas.payload.Parameter
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.http.content.staticFiles
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import java.io.File
import java.net.URI

fun Application.configureRouting(environment: Environment) {
    val publics = listOf(".well-known", "public")

    routing {
        swagger(
            "Lorcana API",
            path = "/"
        )

        publics.forEach { folder ->
            val file = File(folder)
            if (!file.exists()) {
                file.mkdirs()
            }

            staticFiles("/$folder", file) {
                enableAutoHeadResponse()
            }
        }

        route("/cards") {
            install(NotarizedRoute()) {
                get = GetInfo.builder {
                    summary("Get all the cards that are available")
                    description("Will return you the list of cards available for consumption")
                    externalDocumentation(
                        ExternalDocumentation(
                            URI(environment.urlDocumentation),
                            "Get help on Discord"
                        )
                    )
                    response {
                        responseCode(HttpStatusCode.OK)
                        responseType<List<MappingVirtualCard>>()
                        description("Will return the list")
                    }
                }
            }

            get {
                val cards = environment.lorcanaLoaded.cards

                this.call.respond(cards)
            }
        }

        route("/set/{set}") {
            install(NotarizedRoute()) {
                get = GetInfo.builder {
                    summary("Get the cards for a specific set")
                    description("Will return you the map of cards available for the given set")
                    externalDocumentation(
                        ExternalDocumentation(
                            URI(environment.urlDocumentation),
                            "Get help on Discord"
                        )
                    )
                    response {
                        responseCode(HttpStatusCode.OK)
                        responseType<Map<String, MappingVirtualCard>>()
                        description("The Map of id (int not strings*) in the set to the corresponding card")
                    }
                    canRespond {
                        responseCode(HttpStatusCode.NotFound)
                        responseType<Unit>()
                        description("The set is invalid")
                    }
                    parameters = listOf(
                        Parameter(
                            name = "set",
                            required = true,
                            `in` = Parameter.Location.path,
                            schema = TypeDefinition.STRING,
                            description = "A valid set name following the given list : " +
                                    SetDescription.entries.joinToString("<br />") { " - $it" }
                        )
                    )
                }
            }

            get {
                val set = SetDescription.entries.find {
                    it.name.lowercase() == call.parameters["set"]
                }

                if (null == set) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                val cards = environment.lorcanaLoaded.cards.mapNotNull { card ->
                    val variant = card.variants.find { variant ->
                        variant.set == set
                    }

                    if (null != variant) {
                        variant.id to card
                    } else {
                        null
                    }
                }.toMap()

                call.respond(cards)
            }
        }

        route("/artists") {
            artists(environment)
        }

        decks(environment)

        route("/rph") {
            routeMap()
            routeRPH(environment)
        }
    }
}
