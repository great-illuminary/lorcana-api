package eu.codlab.lorcana.api

import eu.codlab.lorcana.api.backend.Backend
import eu.codlab.lorcana.api.environment.Environment
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        val environment = Environment.initialize()

        val backend = Backend(environment)

        try {
            backend.start()
        } catch (err: Throwable) {
            // todo: manage sentry call here
        }
    }
}
