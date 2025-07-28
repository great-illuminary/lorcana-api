package eu.codlab.lorcana.dreamborn.utils

import io.sentry.kotlin.multiplatform.Sentry

@Suppress("TooGenericExceptionCaught")
suspend fun trySentry(
    onError: suspend () -> Unit = {},
    block: suspend () -> Unit
) = try {
    block()
} catch (err: Throwable) {
    Sentry.captureException(err)
    onError()
}

@Suppress("TooGenericExceptionCaught")
fun <T> trySentryRethrow(
    onError: () -> Unit = { },
    block: () -> T
) = try {
    block()
} catch (err: Throwable) {
    Sentry.captureException(err)

    onError()

    throw err
}
