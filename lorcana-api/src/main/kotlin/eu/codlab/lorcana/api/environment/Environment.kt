package eu.codlab.lorcana.api.environment

import eu.codlab.lorcana.Lorcana
import eu.codlab.lorcana.LorcanaLoaded

class Environment private constructor(
    val lorcanaLoaded: LorcanaLoaded,
    val urlDocumentation: String,
    val link: String
) {
    companion object {
        suspend fun initialize(): Environment {
            val loaded = Lorcana().loadFromResources()

            return Environment(
                loaded,
                urlDocumentation = "https://discord.gg/q9JRn8zjRS",
                link = "<a href=\"https://discord.gg/q9JRn8zjRS\" target=\"_blank\">Help on Discord</a>"
            )
        }
    }
}
