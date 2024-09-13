package eu.codlab.lorcana.api.backend

import eu.codlab.files.VirtualFile
import eu.codlab.lorcana.api.assets.Assets
import eu.codlab.lorcana.api.environment.Environment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class Backend(
    private val environment: Environment
) {
    private val context = CoroutineScope(Dispatchers.IO + SupervisorJob())

    suspend fun start() {
        val assets = VirtualFile("./assets")

        if (!assets.exists()) {
            assets.mkdirs()
        }

        Assets.load()

        context.launch {
            embeddedServer(Netty, port = 9999, host = "127.0.0.1") {
                configure()
                openAPI()
                configureRouting(this@Backend.environment)
            }.start(wait = true)
        }
    }
}
