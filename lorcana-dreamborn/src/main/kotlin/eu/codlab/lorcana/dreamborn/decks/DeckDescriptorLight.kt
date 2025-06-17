package eu.codlab.lorcana.dreamborn.decks

import kotlinx.serialization.Serializable

@Serializable
internal data class APIDeckDescriptorLight(
    val id: String,
    val url: String,
    val cards: List<APIDreambornCard>
) {
    fun toPublic(): DeckDescriptorLight {
        val cards = mutableMapOf<String, Long>()
        this.cards.forEach { cards[it.id] = 0 }

        this.cards.forEach {
            cards[it.id] = cards[it.id]!! + 1
        }

        return DeckDescriptorLight(
            id = id,
            url = url,
            cards = cards
        )
    }
}

@Serializable
data class DeckDescriptorLight(
    val id: String,
    val url: String,
    val cards: Map<String, Long>
)

@Serializable
internal data class APIDreambornCard(
    val setId: String,
    val number: String,
    // other data are defined but those are already available in the lorcana-data api
) {
    val id: String = "$setId-${number.padStart(3, '0')}"
}

@Serializable
data class DreambornCard(
    val setId: String,
    val cardNumber: String,
    val quantity: Int
    // other data are defined but those are already available in the lorcana-data api
)
