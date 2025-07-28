package eu.codlab.lorcana.dreamborn.nuxt

import eu.codlab.lorcana.dreamborn.decks.DeckDescriptor
import eu.codlab.lorcana.dreamborn.decks.DeckLinks
import eu.codlab.lorcana.dreamborn.decks.DeckTags
import eu.codlab.lorcana.dreamborn.utils.trySentryRethrow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

internal class NuxtExtractor(fromString: String) {
    private val expectedFirstElementIndex = 4
    private val json = Json
    private val matchString = "\"[^,]*\""
    private val matchArray = "\\[[^\\]]*\\]"
    private val matchJson = "\\{[^\\}]*\\}"
    private val matchBool = "true|false|\\d+"
    private val findInNuxtString = "(($matchArray|$matchJson|$matchString|$matchBool),)".toRegex()

    private val from: String = fromString
        .replace("\n", "")
        .replace("[[", "[")

    private fun findIndexForData(mappedArguments: List<String>): Int? {
        return (0..mappedArguments.size).firstOrNull { index ->
            try {
                json.decodeFromString<ExpectedArgumentIndexesInfo>(mappedArguments[index])
                true
            } catch (_: Throwable) {
                false
            }
        }
    }

    fun extractDeck(): DeckDescriptor = trySentryRethrow(
        onError = { println("issue when decoding $from") }
    ) {
        val found = findInNuxtString.findAll(from)

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
        } catch (_: Throwable) {
            // and if we have an issue, we know it will because of the element in between
            originalMappedArguments.subList(0, expectedFirstElementIndex) +
                    "" +
                    originalMappedArguments.subList(
                        expectedFirstElementIndex,
                        originalMappedArguments.size
                    )
        }

        val cards: Map<String, Int> = json.decodeFromString(mappedArguments[data.cards])
        val links: ExpectedArgumentIndexesLinks =
            try {
                json.decodeFromString(mappedArguments[data.links])
            } catch (_: Throwable) {
                ExpectedArgumentIndexesLinks()
            }
        val youtube = links.youtube?.let { mappedArguments[it].replaceWrap() } ?: ""

        val mappedCards =
            cards.map { (key, value) -> key to mappedArguments[value].toLong() }.toMap()

        val colorsCorrespondance =
            json.decodeFromString<List<Int>>(mappedArguments[data.colors])
                .map { mappedArguments[it] }

        DeckDescriptor(
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

    fun String.replaceWrap() = this.replace("\"", "")
}
