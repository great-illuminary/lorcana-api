package eu.codlab.lorcana.dreamborn.database

import eu.codlab.lorcana.dreamborn.decks.DeckDescriptor
import eu.codlab.lorcana.dreamborn.decks.DeckDescriptorLight
import eu.codlab.lorcana.dreamborn.local.Local_decks
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Deck(
    @Transient
    val id: Long = 0,
    val uuid: String,
    @SerialName("updated_at")
    val updatedAt: String,
    val creator: String,
    @SerialName("creator_name")
    val creatorName: String,
    val youtube: String?,
    val name: String,
    val cardsCount: Long?,
    val views: Long?,
    val likes: Long?,
    var cards: List<DeckCards> = emptyList(),
    var versions: Map<Long, List<DeckCards>> = emptyMap(),
    @SerialName("last_trending_at_ms")
    val lastTrendingAtMs: Long? = null,
    @SerialName("last_checked_at_ms")
    val lastCheckedAtMs: Long? = null,
    @SerialName("is_private")
    val isPrivate: Boolean
) {
    internal fun copyUpdateFromRemote(deck: DeckDescriptor) = copy(
        uuid = deck.id,
        updatedAt = deck.lastUpdated,
        creator = deck.creator,
        creatorName = deck.creatorName,
        youtube = deck.links.youtube,
        name = deck.name,
        cardsCount = deck.size.toLong(),
        views = deck.views.toLong(),
        likes = deck.likeCount.toLong()
    )

    internal fun copyUpdateFromRemote(deck: DeckDescriptorLight) = copy(
        uuid = deck.id,
        cardsCount = deck.cards.values.sum().toLong(),
    )

    companion object {
        internal fun from(localDeck: Local_decks) = Deck(
            id = localDeck.id,
            uuid = localDeck.uuid,
            updatedAt = localDeck.updated_at,
            creator = localDeck.creator,
            creatorName = localDeck.creator_name,
            youtube = localDeck.youtube,
            name = localDeck.name,
            cardsCount = localDeck.cards,
            views = localDeck.views,
            likes = localDeck.likes,
            lastTrendingAtMs = localDeck.last_trending_at_ms ?: 0,
            lastCheckedAtMs = localDeck.last_checked_at_ms ?: 0,
            isPrivate = localDeck.is_private == 1L
        )
    }
}


data class MappingDeck(
    val id: Long = 0,
    val uuid: String,
    @SerialName("updated_at")
    val updatedAt: String,
    val creator: String,
    @SerialName("creator_name")
    val creatorName: String,
    val youtube: String?,
    val name: String,
    val cardsCount: Long?,
    val views: Long?,
    val likes: Long?,
    var cards: List<DeckCards> = emptyList(),
    var versions: Map<String, List<DeckCards>> = emptyMap(),
    @SerialName("last_trending_at_ms")
    val lastTrendingAtMs: Long? = null,
    @SerialName("last_checked_at_ms")
    val lastCheckedAtMs: Long? = null,
    @SerialName("is_private")
    val isPrivate: Boolean
)