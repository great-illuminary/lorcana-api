package eu.codlab.lorcana.api.backend

import io.bkbn.kompendium.core.plugin.NotarizedApplication
import io.bkbn.kompendium.oas.OpenApiSpec
import io.bkbn.kompendium.oas.info.Info
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.openAPI() {
    install(NotarizedApplication()) {
        spec = {
            OpenApiSpec(
                openapi = "3.1.1",
                info = Info(
                    title = "Lorcana API",
                    version = "0.0.1"
                )
            )
        }
        specRoute = { spec, routing ->
            routing {
                route("/openapi.json") {
                    get {
                        call.respond(
                            HttpStatusCode.OK,
                            spec
                        )
                    }
                }
            }
        }
    }
}
