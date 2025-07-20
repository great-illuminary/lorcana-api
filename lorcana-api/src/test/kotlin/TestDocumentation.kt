import eu.codlab.http.createClient
import eu.codlab.lorcana.api.backend.configure
import eu.codlab.lorcana.api.backend.configureRouting
import eu.codlab.lorcana.api.backend.openAPI
import eu.codlab.lorcana.api.environment.Environment
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

class TestDocumentation {
    @Test
    fun testBackend() {
        runBlocking {
            val environment = Environment.initialize()

            embeddedServer(Netty, port = 9999, host = "127.0.0.1") {
                openAPI()
                configure()
                configureRouting(environment)
            }.start(wait = false)

            delay(1.seconds)

            val client = createClient { }

            val req = client.get("http://127.0.0.1:8080/openapi.json")
            val res: String = req.body()
            println(res)
        }
    }
}
