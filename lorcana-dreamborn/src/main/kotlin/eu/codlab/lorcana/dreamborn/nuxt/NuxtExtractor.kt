package eu.codlab.lorcana.dreamborn.nuxt

import eu.codlab.lorcana.dreamborn.decks.DeckDescriptor
import eu.codlab.lorcana.dreamborn.decks.DeckLinks
import eu.codlab.lorcana.dreamborn.decks.DeckTags
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

internal class NuxtExtractor(fromString: String) {
    private val json = Json

    private val from: String = fromString
        .replace("\n", "")
        .replace("[[", "[")

    private fun findIndexForData(mappedArguments: List<String>): Int? {
        return (0..mappedArguments.size).firstOrNull { index ->
            try {
                json.decodeFromString<ExpectedArgumentIndexesInfo>(mappedArguments[index])
                true
            } catch (err: Throwable) {
                false
            }
        }
    }

    fun extractDeck(): DeckDescriptor {
        try {
            val sMatchString = "\"[^,]*\""
            val sMatchArray = "\\[[^\\]]*\\]"
            val sMatchJson = "\\{[^\\}]*\\}"
            val sMatchBool = "true|false|\\d+"

            val regexp = "(($sMatchArray|$sMatchJson|$sMatchString|$sMatchBool),)".toRegex()
            val found = regexp.findAll(from)

            val originalMappedArguments =
                found.map { matchResult -> matchResult.groupValues[2] }.toList()

            originalMappedArguments.forEachIndexed { index, value -> println("$index -> $value") }

            val indexForDeckData =
                findIndexForData(originalMappedArguments) ?: throw IllegalStateException(
                    "Couldn't find valid index for data corresponding to $from"
                )

            val data: ExpectedArgumentIndexesInfo =
                json.decodeFromString(originalMappedArguments[indexForDeckData])

            // manage an edge case where the index were shift by 1 -> since it would introduce more issues
            //   just bypass this
            val mappedArguments = try {
                // first attempt to decode the cards
                json.decodeFromString<Map<String, Int>>(originalMappedArguments[data.cards])
                originalMappedArguments
            } catch (err: Throwable) {
                // and if we have an issue, we know it will because of the element in between
                originalMappedArguments.subList(0, 4) +
                        "" +
                        originalMappedArguments.subList(4, originalMappedArguments.size)
            }

            val cards: Map<String, Int> = json.decodeFromString(mappedArguments[data.cards])
            val links: ExpectedArgumentIndexesLinks =
                try {
                    json.decodeFromString(mappedArguments[data.links])
                } catch (err: Throwable) {
                    // nothing
                    ExpectedArgumentIndexesLinks()
                }
            val youtube = links.youtube?.let { mappedArguments[it].replaceWrap() } ?: ""

            val mappedCards =
                cards.map { (key, value) -> key to mappedArguments[value].toLong() }.toMap()

            println(data)
            println(mappedCards)

            val colorsCorrespondance =
                json.decodeFromString<List<Int>>(mappedArguments[data.colors])
                    .map { mappedArguments[it] }

            val deck = DeckDescriptor(
                id = mappedArguments[data.id].replaceWrap(),
                name = mappedArguments[data.name].replaceWrap(),
                creator = mappedArguments[data.creator].replaceWrap(),
                creatorName = mappedArguments[data.creatorName].replaceWrap(),
                colors = colorsCorrespondance,
                size = mappedArguments[data.size].toInt(),
                lastUpdated = mappedArguments[data.lastUpdated].replaceWrap(),
                cards = mappedCards,
                isPublic = "true" == mappedArguments[data.isPublic],
                likeCount = mappedArguments[data.likeCount].toInt(),
                views = mappedArguments[data.views].toInt(),
                totalPrice = mappedArguments[data.totalPrice].toDouble(),
                tags = DeckTags(), // mappedArguments[data.tags]
                links = DeckLinks(youtube = youtube)
            )

            return deck
        } catch (err: Throwable) {
            println("issue when decoding $from")
            println("having issue -> ")
            err.printStackTrace()

            throw err
        }
    }

    @Serializable
    private data class ExpectedArgumentIndexesLinks(
        val youtube: Int? = null
    )

    @Serializable
    private data class ExpectedArgumentIndexesInfo(
        val id: Int,
        val name: Int,
        val creator: Int,
        val creatorName: Int,
        val colors: Int,
        val size: Int,
        val lastUpdated: Int,
        val cards: Int,
        val isPublic: Int,
        val liked: Int,
        val likeCount: Int,
        val views: Int,
        val totalPrice: Int,
        val missingPrice: Int,
        val description: Int,
        val tags: Int,
        val links: Int,
        val pbCode: Int
    )

    private data class ExpectedArgumentIndexesDiagnostic(
        val data: Int,
        val state: Int,
        val once: Int,
        val _errors: Int,
        val serverRendered: Int,
        val path: Int,
        val pinia: Int,
    )

    fun String.replaceWrap() = this.replace("\"", "")
}
