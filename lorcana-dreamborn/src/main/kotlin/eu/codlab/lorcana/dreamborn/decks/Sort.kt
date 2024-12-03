package eu.codlab.lorcana.dreamborn.decks

enum class Sort {
    Newest,
    Videos,
    Popular,
    Trending;

    val apiName = name.lowercase()
}
