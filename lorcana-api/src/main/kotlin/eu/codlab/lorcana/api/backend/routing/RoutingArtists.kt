package eu.codlab.lorcana.api.backend.routing

import eu.codlab.lorcana.api.environment.Environment
import eu.codlab.lorcana.api.holders.artists.ArtistHolder
import eu.codlab.lorcana.raw.VirtualCard
import io.bkbn.kompendium.core.metadata.GetInfo
import io.bkbn.kompendium.core.plugin.NotarizedRoute
import io.bkbn.kompendium.oas.common.ExternalDocumentation
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import java.net.URI

fun Route.artists(environment: Environment) {
    val artistsHolder = ArtistHolder(environment.lorcanaLoaded)

    route("/cards") {
        install(NotarizedRoute()) {
            get = GetInfo.builder {
                summary("Retrieve the cards for each artists")
                description("Will return a map of artists to their corresponding stats")
                externalDocumentation(
                    ExternalDocumentation(
                        URI(environment.urlDocumentation),
                        "Get help on Discord"
                    )
                )
                response {
                    responseCode(HttpStatusCode.OK)
                    responseType<Map<Int, VirtualCard>>()
                    description("The Map of artist in the set mapped to their cards")
                }
            }
        }

        get {
            call.respond(artistsHolder.pairs)
        }
    }

    route("/stats") {
        install(NotarizedRoute()) {
            get = GetInfo.builder {
                summary("Retrieve the stats for each artists")
                description("Will return a map of artists to their corresponding stats")
                externalDocumentation(
                    ExternalDocumentation(
                        URI(environment.urlDocumentation),
                        "Get help on Discord"
                    )
                )
                response {
                    responseCode(HttpStatusCode.OK)
                    responseType<Map<Int, VirtualCard>>()
                    description("The Map of artist in the set to the corresponding stat")
                }
            }
        }

        get {
            call.respond(artistsHolder.pairsCount)
        }
    }
}
