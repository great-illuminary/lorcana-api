package eu.codlab.lorcana.rph.utils

import eu.codlab.files.VirtualFile
import eu.codlab.http.Configuration
import eu.codlab.http.ProxyConfiguration
import eu.codlab.http.createClient
import eu.codlab.lorcana.dreamborn.Config
import io.ktor.client.call.body
import io.ktor.client.engine.ProxyBuilder
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.Url
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json
import java.util.Base64

internal object Clients {
    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = false
        prettyPrint = true
    }

    val client = createClient(
        Configuration(
            json = json,
            enableLogs = false,
            socketTimeoutMillis = 30000,
            connectTimeoutMillis = 30000,
            requestTimeoutMillis = 30000
        )
    ) { }
    val clientWithProxy = createClient(
        Configuration(
            json = json,
            enableLogs = true,
            socketTimeoutMillis = 30000,
            connectTimeoutMillis = 30000,
            requestTimeoutMillis = 30000,
            proxyConfiguration = ProxyConfiguration(
                proxyConfig = ProxyBuilder.http(Url("http://api.zenrows.com:8001")),
                proxyAuthentication = {
                    Config.zenrows to "js_render=true&premium_proxy=true&proxy_country=fr"
                },
                trustCertificatesOnAndroidJvm = true
            ),
        )
    ) { }

    suspend inline fun <reified T> get(url: String): T {
        //TODO manage issues which are striclty related to the data mapping
        val answer = client.get(url)

        if (answer.status.isSuccess()) {
            // return answer.body()
            val file = VirtualFile(VirtualFile.Root, "dump.txt")
            val text = answer.bodyAsText()
            file.write(text.toByteArray())
            return json.decodeFromString(text)
        }

        try {
            val request = clientWithProxy.get(url) {
                val clientInfo =
                    "${Config.zenrows}:js_render=true&premium_proxy=true&proxy_country=fr"
                val basicAuth = Base64.getEncoder().encodeToString(clientInfo.toByteArray())
                header(HttpHeaders.ProxyAuthorization, "Basic $basicAuth")
            }
            if (request.status.isSuccess()) {
                return request.body()
            }
        } catch (err: HttpRequestTimeoutException) {
            // nothing
        }
        throw IllegalStateException("Couldn't load $url")
    }
}