package eu.codlab.lorcana.api.environment

import eu.codlab.lorcana.Lorcana
import eu.codlab.lorcana.LorcanaLoaded
import eu.codlab.lorcana.dreamborn.Dreamborn
import eu.codlab.lorcana.rph.RavensburgerController
import io.sentry.kotlin.multiplatform.Sentry

class Environment private constructor(
    val lorcanaLoaded: LorcanaLoaded,
    val urlDocumentation: String,
    val link: String,
    val dreamborn: Dreamborn,
    val ravensburgerController: RavensburgerController
) {
    companion object {
        suspend fun initialize(): Environment {
            val loaded = Lorcana().loadFromResources()

            return Environment(
                loaded,
                urlDocumentation = "https://discord.gg/q9JRn8zjRS",
                link = "<a href=\"https://discord.gg/q9JRn8zjRS\" target=\"_blank\">Help on Discord</a>",
                dreamborn = Dreamborn(),
                ravensburgerController = RavensburgerController {
                    Sentry.captureException(it)
                    it.printStackTrace()
                }
            )
        }
    }
}
