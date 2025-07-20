package eu.codlab.lorcana.api.utils

import io.bkbn.kompendium.core.plugin.NotarizedRoute
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingContext
import io.sentry.kotlin.multiplatform.Sentry

fun Route.installWithOpenApiCategory(
    category: String,
    body: NotarizedRoute.Config.() -> Unit
) = install(NotarizedRoute()) {
    tags = setOf(category)

    body(this)
}

@Suppress("TooGenericExceptionCaught")
suspend fun RoutingContext.trySentry(
    onError: suspend RoutingContext.() -> Unit = {},
    body: suspend RoutingContext.() -> Unit
) {
    try {
        body(this)
    } catch (err: Throwable) {
        Sentry.captureException(err)
        onError()
    }
}
