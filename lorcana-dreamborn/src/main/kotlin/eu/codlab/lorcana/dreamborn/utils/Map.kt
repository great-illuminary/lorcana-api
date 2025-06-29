package eu.codlab.lorcana.dreamborn.utils

import eu.codlab.lorcana.dreamborn.database.DeckCards

/**
 * Get the current key->cardinal code from the key natural order
 *
 * This can be used to compare 2 versions of a deck to check if a new version is available
 */
fun Map<String, Long>.code() =
    this.entries.map { "${it.key}_${it.value}" }
        .sortedBy { it }.joinToString("_").hashCode()

/**
 * Get the current key->cardinal code from the key natural order
 *
 * This can be used to compare 2 versions of a deck to check if a new version is available
 */
fun List<DeckCards>.code() = this.map { "${it.dreamborn}_${it.count}" }
    .sortedBy { it }.joinToString("_").hashCode()
