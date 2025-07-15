package eu.codlab.lorcana.rph.store

import eu.codlab.lorcana.rph.utils.Coordinates
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
    val longitude: Double? = null,

    /**
     * Duplicated information from above two. Only available in the full list of stores
     */
    val coordinates: LatLng? = null,

    /**
     * Only available in the full list of stores
     */
    val email: String? = null,

    /**
     * Only available in the full list of stores
     */
    @SerialName("street_address")
    val streetAddress: String? = null,

    /**
     * Only available in the full list of stores
     */
    val zipcode: String? = null,

    /**
     * Only available in the full list of stores
     */
    @SerialName("phone_number")
    val phoneNumber: String? = null,

    /**
     * Only available in the full list of stores
     */
    @SerialName("store_types")
    val storeTypes: List<String>? = null,

    /**
     * Only available in the full list of stores
     */
    @SerialName("store_types_pretty")
    val storeTypesPretty: List<String>? = null,
) {
    fun id() = id
}

@Serializable
data class LatLng(
    @SerialName("lat")
    val latitude: Double? = null,
    @SerialName("lng")
    val longitude: Double? = null
)