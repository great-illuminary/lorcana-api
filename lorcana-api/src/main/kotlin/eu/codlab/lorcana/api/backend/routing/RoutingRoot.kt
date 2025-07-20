package eu.codlab.lorcana.api.backend.routing

import eu.codlab.lorcana.api.environment.Environment
import eu.codlab.lorcana.api.environment.discord
import eu.codlab.lorcana.api.utils.installWithOpenApiCategory
import eu.codlab.lorcana.raw.SetDescription
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
import java.net.URI

@Suppress("LongMethod")
fun Route.root(environment: Environment) {
    fun Route.overrideInstall(body: NotarizedRoute.Config.() -> Unit) =
        installWithOpenApiCategory("Cards", body)

    route("/cards") {
        overrideInstall {
            get = GetInfo.builder {
                summary("Get all the cards that are available")
                description("Will return you the list of cards available for consumption")

                discord(environment)

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
        overrideInstall {
            get = GetInfo.builder {
                summary("Get the cards for a specific set")
                description("Will return you the map of cards available for the given set")

                discord(environment)

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
}
