package eu.codlab.lorcana.api.holders.artists

import eu.codlab.lorcana.LorcanaLoaded

class ArtistHolder(
    private val lorcana: LorcanaLoaded
) {
    val pairs = mutableMapOf<String, MutableList<VariantPrimary>>()
    val pairsCount: Map<String, Int>

    init {
        lorcana.cards.map { card ->
            card.variants.forEach { variant ->
                val illustrator = variant.illustrator ?: card.illustrator

                illustrator.split(" / ").forEach { split ->
                    pairs.putIfAbsent(split, mutableListOf())
                    pairs[split]!!.add(VariantPrimary(variant, card))
                }
            }
        }

        pairsCount = pairs.map { (key, value) -> key to value.size }
            .toMap().toSortedMap().toMap()
    }
}
