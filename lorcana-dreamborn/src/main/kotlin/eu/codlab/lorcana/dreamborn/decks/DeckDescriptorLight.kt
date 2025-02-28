package eu.codlab.lorcana.dreamborn.decks

import kotlinx.serialization.Serializable

@Serializable
data class DeckDescriptorLight(
    val id: String,
    val url: String,
    val cards: List<DreambornCard>
)

@Serializable
data class DreambornCard(
    val setId: String,
    val number: String,
    // other data are defined but those are already available in the lorcana-data api
)
