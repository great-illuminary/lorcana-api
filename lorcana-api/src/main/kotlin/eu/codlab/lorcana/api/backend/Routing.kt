package eu.codlab.lorcana.api.backend

import eu.codlab.lorcana.api.backend.routing.artists
import eu.codlab.lorcana.api.backend.routing.decks
import eu.codlab.lorcana.api.backend.routing.root
import eu.codlab.lorcana.api.backend.routing.routeMap
import eu.codlab.lorcana.api.backend.routing.routeRPH
import eu.codlab.lorcana.api.backend.routing.routeRPHRaw
import eu.codlab.lorcana.api.environment.Environment
import io.bkbn.kompendium.core.routes.swagger
import io.ktor.server.application.Application
import io.ktor.server.http.content.staticFiles
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import java.io.File

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

        root(environment)

        route("/artists") {
            artists(environment)
        }

        decks(environment)

        route("/rph") {
            routeMap()
            routeRPH(environment)
            routeRPHRaw(environment)
        }
    }
}
