package eu.codlab.lorcana.dreamborn.database

import eu.codlab.lorcana.dreamborn.decks.DeckDescriptor
import eu.codlab.lorcana.dreamborn.local.LocalDatabase
import korlibs.time.DateTime

internal class LocalDecksController internal constructor(
    private val database: LocalDatabase
) {
    private val mutableCardLists: MutableMap<Long, MutableMap<Long, List<DeckCards>>> =
        mutableMapOf()

    init {
        val decks = selectAll()
        decks.forEach { deck ->
            mutableCardLists[deck.id] = cardsAllVersions(deck)
        }

        database.localDeckVersionsQueries.let {
            it.checkIndexDeckId()
            it.checkIndexCreatedAt()
        }

        database.localDecksQueries.let {
            it.checkIndexUuid()
            it.checkIndexLikes()
            it.checkIndexViews()
            it.checkIndexCreator()
        }

        database.localDeckCardsQueries.let {
            it.checkIndexDeckId()
            it.checkIndexSetNumber()
            it.checkIndexDeckVersionId()
            it.checkIndexDefaultSetNumber()
        }
    }

    fun selectAll() = database.localDecksQueries.selectAll()
        .executeAsList().map { Deck.from(it) }

    fun selectAllWithCards() = selectAll().map {
        it.cards = cacheLastVersion(it) ?: emptyList()
        it.versions = mutableCardLists[it.id] ?: emptyMap()
        it
    }

    fun selectedFromUuid(uuid: String) = database.localDecksQueries.selectFromUUID(uuid)
        .executeAsList().map { Deck.from(it) }.firstOrNull()

    fun selectedFromUuidWithCards(uuid: String): Deck? {
        return selectedFromUuid(uuid)?.apply {
            this.cards = cacheLastVersion(this) ?: emptyList()
            this.versions = mutableCardLists[this.id] ?: emptyMap()
        }
    }

    fun selectedFromCreator(uuid: String) = database.localDecksQueries.selectFromCreator(uuid)
        .executeAsList().map {
            Deck.from(it).apply {
                this.cards = cacheLastVersion(this) ?: emptyList()
                this.versions = mutableCardLists[it.id] ?: emptyMap()
            }
        }

    fun update(deck: Deck, cards: Map<String, Long>, updatedAt: DateTime) {
        database.localDecksQueries.update(
            updated_at = deck.updatedAt,
            cards = deck.cardsCount,
            views = deck.views,
            likes = deck.likes,
            id = deck.id
        )

        // now set the cards
        val createdAt = updatedAt.unixMillisLong
        database.localDeckVersionsQueries.insert(deck.id, createdAt)
        val version = database.localDeckVersionsQueries.selectFromUpdatedAt(deck.id, createdAt)
            .executeAsList().first()

        cards.forEach {
            database.localDeckCardsQueries.insert(
                deck_id = deck.id,
                deck_version_id = version.id,
                dreamborn_id = it.key,
                default_dreamborn_id = it.key, // in the future the default will be the non enchanted
                count = it.value,
            )
        }
        mutableCardLists.putIfAbsent(deck.id, mutableMapOf())
        mutableCardLists[deck.id]?.set(
            createdAt,
            cards.map {
                DeckCards(
                    version.id,
                    dreamborn = it.key,
                    count = it.value
                )
            }
        )
    }

    fun cardsAllVersions(deck: Deck): MutableMap<Long, List<DeckCards>> {
        val rawVersions = database.localDeckVersionsQueries
            .selectFromDeckId(deck.id)
            .executeAsList()

        val result = mutableMapOf<Long, List<DeckCards>>()
        rawVersions.forEach { version ->
            val cards = database.localDeckCardsQueries
                .selectFromDeckIdAndVersion(deck.id, version.id)
                .executeAsList().map {
                    DeckCards(
                        version = it.deck_version_id ?: -1,
                        dreamborn = it.dreamborn_id,
                        count = it.count ?: 0
                    )
                }

            result[version.created_at] = cards
        }

        return result
    }

    fun cards(deck: Deck) = database.localDeckCardsQueries
        .selectFromDeckIdLastVersion(deck.id, deck.id)
        .executeAsList().map {
            DeckCards(
                dreamborn = it.dreamborn_id,
                count = it.count ?: 0
            )
        }

    fun insert(
        deck: DeckDescriptor,
        cards: Map<String, Long>,
        createdAt: DateTime,
        lastTrendingAt: DateTime? = null,
        lastCheckedAt: DateTime,
        isPrivate: Boolean
    ) {
        database.localDecksQueries.insert(
            uuid = deck.id,
            updated_at = deck.lastUpdated,
            creator = deck.creator,
            creator_name = deck.creatorName,
            youtube = deck.links.youtube,
            name = deck.name,
            cards = deck.size.toLong(),
            views = deck.views.toLong(),
            likes = deck.likeCount.toLong(),
            last_trending_at_ms = lastTrendingAt?.unixMillisLong,
            last_checked_at_ms = lastCheckedAt.unixMillisLong,
            is_private = if (isPrivate) 1 else 0
        )

        val inserted = database.localDecksQueries.selectFromUUID(deck.id)
            .executeAsList().firstOrNull()
            ?: throw IllegalStateException("The deck was not inserted")

        val createdMs = createdAt.unixMillisLong
        database.localDeckVersionsQueries.insert(inserted.id, createdMs)
        val version = database.localDeckVersionsQueries.selectFromUpdatedAt(inserted.id, createdMs)
            .executeAsList().first()

        cards.forEach {
            database.localDeckCardsQueries.insert(
                deck_id = inserted.id,
                deck_version_id = version.id,
                dreamborn_id = it.key,
                count = it.value,
                default_dreamborn_id = it.key
            )
        }
        mutableCardLists.putIfAbsent(inserted.id, mutableMapOf())
        mutableCardLists[inserted.id]?.set(
            createdMs,
            cards.map {
                DeckCards(
                    version.id,
                    dreamborn = it.key,
                    count = it.value
                )
            }
        )
    }

    fun updateLastCheckedAt(deck: Deck, lastChecked: DateTime) =
        database.localDecksQueries.updateLastCheckedAt(lastChecked.unixMillisLong, deck.id)

    fun updateLastTrendingAt(deck: Deck, lastTrending: DateTime) =
        database.localDecksQueries.updateLastTrendingAt(lastTrending.unixMillisLong, deck.id)

    fun updateIsPrivate(deck: Deck, isPrivate: Boolean) =
        database.localDecksQueries.updatePrivate(if (isPrivate) 1 else 0, deck.id)

    private fun cacheLastVersion(deck: Deck): List<DeckCards>? {
        return mutableCardLists[deck.id]?.keys?.maxOrNull()
            ?.let { mutableCardLists[deck.id]?.get(it) }
    }
}
