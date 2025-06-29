package eu.codlab.lorcana.rph.store

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoreFull(
    val id: Long,
    val name: String,
    @SerialName("full_address")
    val fullAddress: String,
    @SerialName("administrative_area_level_1_short")
    val administrativeAreaLevel1Short: String? = null,
    /**
     * Store's country. Unavailable in the list of all events
     */
    val country: String? = null,
    /**
     * Store's website. Unavailable in the list of all events
     */
    val website: String? = null,
    /**
     * Store's latitude. Unavailable in the list of all events
     */
    val latitude: Double? = null,
    /**
     * Store's longitude. Unavailable in the list of all events
     */
    val longitude: Double? = null
) {
    fun id() = id
}
