package eu.codlab.lorcana.api.environment

import io.bkbn.kompendium.core.metadata.MethodInfo
import io.bkbn.kompendium.oas.common.ExternalDocumentation
import java.net.URI

fun MethodInfo.Builder<*>.discord(environment: Environment) = run {
    externalDocumentation(
        ExternalDocumentation(
            URI(environment.urlDocumentation),
            "Get help on Discord"
        )
    )
}