package eu.codlab.lorcana.rph.game

import kotlinx.serialization.Serializable

@Serializable
data class Game(
    /**
     * Represents the game description's database id
     */
    val id: Int,

    /**
     * Represents the game description's name
     */
    val name: String,

    /**
     * Represents the game description's slug - the "human" readable identifier
     */
    val slug: String
)
