package eu.codlab.lorcana.dreamborn

import eu.codlab.http.Configuration
import eu.codlab.http.ProxyConfiguration
import eu.codlab.http.createClient
import eu.codlab.lorcana.dreamborn.decks.APIDeckDescriptorLight
import eu.codlab.lorcana.dreamborn.decks.CreatorDescriptor
import eu.codlab.lorcana.dreamborn.decks.DeckDescriptor
import eu.codlab.lorcana.dreamborn.decks.DeckDescriptorLight
import eu.codlab.lorcana.dreamborn.decks.Sort
import eu.codlab.lorcana.dreamborn.nuxt.NuxtExtractor
import io.ktor.client.call.body
import io.ktor.client.engine.ProxyBuilder
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.Url
import io.ktor.http.isSuccess
import io.ktor.http.userAgent
import java.util.Base64

internal class DreambornApi {
    private val client = createClient(
        Configuration(
            socketTimeoutMillis = 30000,
            connectTimeoutMillis = 30000,
            requestTimeoutMillis = 30000
        )
    ) { }

    private val clientWithProxy = createClient(
        Configuration(
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
    private val url = "https://dreamborn.ink/api"

    suspend fun creator(
        uuid: String,
        ssr: Int = 1,
        currency: String? = null
    ): CreatorDescriptor {
        val suffix = mapArguments("ssr" to ssr, "currency" to currency)

        return client.get("$url/_ssr/users/$uuid?$suffix") {
            configureHeaders()
        }.body()
    }

    suspend fun decks(
        sort: Sort? = null,
        currency: String? = null
    ) = decks(sort?.apiName, currency)

    suspend fun decks(
        sort: String? = null,
        currency: String? = null
    ): List<DeckDescriptor> {
        val suffix = mapArguments("sort" to sort, "currency" to currency)

        try {
            return client.get("$url/decks/?$suffix&archetype") {
                configureHeaders()
            }.body()
        } catch (_: Throwable) {
            // nothing
        }

        val dreambornUrlPart = "$url/decks/?$suffix&archetype"
        return get<List<DeckDescriptor>>(dreambornUrlPart)
            ?: throw IllegalStateException("Couldn't load info")
    }

    private suspend inline fun <reified T> get(url: String): T? = try {
        val request = clientWithProxy.get(url) {
            val clientInfo =
                "${Config.zenrows}:js_render=true&premium_proxy=true&proxy_country=fr"
            val basicAuth = Base64.getEncoder().encodeToString(clientInfo.toByteArray())
            header(HttpHeaders.ProxyAuthorization, "Basic $basicAuth")
        }
        if (request.status.isSuccess()) {
            request.body()
        } else {
            null
        }
    } catch (_: HttpRequestTimeoutException) {
        // nothing
        null
    } catch (_: Throwable) {
        // nothing
        null
    }

    suspend fun deckLight(deck: String): DeckDescriptorLight? {
        return try {
            client.get("$url/_tts/decks/$deck") { userAgent("Tabletop Simulator") }
                .let { if (it.status.isSuccess()) it else throw IllegalStateException("Couldn't load") }
                .body<APIDeckDescriptorLight?>()?.toPublic()
        } catch (_: Throwable) {
            // nothing
            null
        }
    }

    suspend fun deck(deck: String, currency: String): DeckDescriptor? {
        try {
            return client.get("$url/decks/$deck?currency=$currency")
                .let { if (it.status.isSuccess()) it else throw IllegalStateException("Couldn't load") }
                .body()
        } catch (_: Throwable) {
            // nothing
        }

        @Suppress("TooGenericExceptionCaught")
        return try {
            val dreambornUrlPart = "https://dreamborn.ink/decks/$deck"
            val source: String = get<String>(dreambornUrlPart)
                ?: throw IllegalStateException("Couldn't load info")
            val pattern = "__NUXT_DATA__\">([\\s\\S]*)<"
            val regexp = pattern.toRegex()

            val (nuxtData) = regexp.find(source)!!.destructured
            val extractor = NuxtExtractor(nuxtData)

            extractor.extractDeck()
        } catch (_: Throwable) {
            null
        }
    }

    suspend fun deck(deck: DeckDescriptor, currency: String) = deck(deck.id, currency)

    private fun mapArguments(vararg args: Pair<String, Any?>) =
        args.mapNotNull { (name, value) -> if (null != value) "$name=$value" else null }
            .joinToString("&")

    private fun HttpRequestBuilder.configureHeaders(referer: String? = null) {
        header("Pragma", "no-cache")
        header("Accept", "*/*")
        header("Sec-Fetch-Site", "same-origin")
        header("Accept-Language", "en-EN,en;q=0.9")
        header("Cache-Control", "no-cache")
        header("Sec-Fetch-Mode", "cors")
        header(
            "User-Agent",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.5 Safari/605.1.15"
        )
        if (null != referer) {
            header("Referer", referer)
        }

        header("Sec-Fetch-Dest", "empty")
    }
}
