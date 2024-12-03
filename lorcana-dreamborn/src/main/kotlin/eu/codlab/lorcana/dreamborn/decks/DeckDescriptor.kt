package eu.codlab.lorcana.dreamborn.decks

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class DeckDescriptor(
    val id: String,
    val name: String,
    val creator: String,
    val creatorName: String,
    val colors: List<String>,
    val size: Int,
    val lastUpdated: String,
    val cards: Map<String, Long>,
    val isPublic: Boolean,
    // val liked: Boolean, -> given but not connected so it's always false
    val likeCount: Int,
    val views: Int,
    val totalPrice: Double,
    val tags: DeckTags,
    val links: DeckLinks,
    val pbCode: String? = null
)

@Serializable
data class DeckTags(
    @SerialName("archetype:competitive")
    val competitive: Boolean = false,
    @SerialName("archetype:budget")
    val budget: Boolean = false,
    @SerialName("archetype:control")
    val control: Boolean = false,
    @SerialName("archetype:multiplayer")
    val multiplayer: Boolean = false,
    @SerialName("archetype:midrange")
    val midrange: Boolean = false,
    @SerialName("archetype:casual")
    val casual: Boolean = false,
    @SerialName("archetype:aggro")
    val aggro: Boolean = false,
    @SerialName("archetype:combo")
    val combo: Boolean = false
)

@Serializable
data class DeckLinks(
    val youtube: String? = null
)
