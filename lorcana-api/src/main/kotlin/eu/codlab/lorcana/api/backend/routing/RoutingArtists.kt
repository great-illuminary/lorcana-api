package eu.codlab.lorcana.api.backend.routing

import eu.codlab.lorcana.api.environment.Environment
import eu.codlab.lorcana.api.environment.discord
import eu.codlab.lorcana.api.holders.artists.ArtistHolder
import eu.codlab.lorcana.api.utils.installWithOpenApiCategory
import io.bkbn.kompendium.core.metadata.GetInfo
import io.bkbn.kompendium.core.plugin.NotarizedRoute
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.artists(environment: Environment) {
    fun Route.overrideInstall(body: NotarizedRoute.Config.() -> Unit) =
        installWithOpenApiCategory("Artists", body)

    val artistsHolder = ArtistHolder(environment.lorcanaLoaded)

    route("/cards") {
        overrideInstall {
            get = GetInfo.builder {
                summary("Retrieve the cards for each artists")
                description("Will return a map of artists to their corresponding stats")

                discord(environment)

                response {
                    responseCode(HttpStatusCode.OK)
                    responseType<Map<String, MappingVirtualCard>>()
                    description("The Map of artist in the set mapped to their cards")
                }
            }
        }

        get {
            call.respond(artistsHolder.pairs)
        }
    }

    route("/stats") {
        overrideInstall {
            get = GetInfo.builder {
                summary("Retrieve the stats for each artists")
                description("Will return a map of artists to their corresponding stats")

                discord(environment)

                response {
                    responseCode(HttpStatusCode.OK)
                    responseType<Map<String, Int>>()
                    description("The Map of artist in the set to the corresponding stat")
                }
            }
        }

        get {
            call.respond(artistsHolder.pairsCount)
        }
    }
}
