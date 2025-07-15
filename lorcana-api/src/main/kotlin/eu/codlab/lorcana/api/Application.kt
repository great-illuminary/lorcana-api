package eu.codlab.lorcana.api

import eu.codlab.lorcana.api.backend.Backend
import eu.codlab.lorcana.api.environment.Environment
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.minutes

fun main() {
    runBlocking {
        val environment = Environment.initialize()

        val backend = Backend(environment)

        async {
            while (true) {
                environment.dreamborn.checkDecks()
                delay(30.minutes)
            }
        }

        async {
            environment.ravensburgerController.startSync()
        }

        try {
            backend.start()
        } catch (err: Throwable) {
            // todo: manage sentry call here
        }
    }
}
