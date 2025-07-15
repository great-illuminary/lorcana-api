package eu.codlab.lorcana.rph.store

import eu.codlab.lorcana.rph.game.Game
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoreFullRestLine(
    /**
     * Represent the UUID of the entry
     */
    val id: String,

    /**
     * Represents the game that was actually given to the API call
     */
    val game: Game,

    /**
     * Holds all the store's information
     */
    val store: StoreFull,

    /**
     * Holds the stores's information's list of types which can be displayed right away
     */
    @SerialName("store_types_pretty")
    val storeTypesPretty: List<String>? = null,

    /**
     * Holds the stores's information's list of types which can be enumed
     */
    @SerialName("store_types")
    val storeTypes: List<String>? = null,

    /**
     * When was this entry created
     */
    @SerialName("created_at")
    val createdAt: String,

    /**
     * When was it updated on the backend side
     */
    @SerialName("updated_at")
    val updatedAt: String
)
