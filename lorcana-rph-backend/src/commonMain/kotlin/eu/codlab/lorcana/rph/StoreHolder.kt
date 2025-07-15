package eu.codlab.lorcana.rph

import kotlinx.serialization.Serializable

@Serializable
data class StoreHolder(
    val id: Long,
    val name: String,
    val fullAddress: String,
    val administrativeAreaLevel1Short: String? = null,
    val country: String? = null,
    val website: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val email: String? = null,
    val streetAddress: String? = null,
    val zipcode: String? = null,
    val phoneNumber: String? = null,
    val storeTypes: List<String> = emptyList(),
    val storeTypesPretty: List<String> = emptyList(),
)
