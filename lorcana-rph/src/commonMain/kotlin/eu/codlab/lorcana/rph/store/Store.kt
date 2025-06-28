package eu.codlab.lorcana.rph.store

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed class AbstractStore {
    abstract fun id(): Long
}

@Serializable
data class Store(
    val id: Long,
    val name: String,
    @SerialName("full_address")
    val fullAddress: String,
    val country: String? = null,
    val website: String? = null
) : AbstractStore() {
    override fun id() = id
}

@Serializable
data class StoreFull(
    val id: Long,
    val name: String,
    @SerialName("full_address")
    val fullAddress: String,
    @SerialName("administrative_area_level_1_short")
    val administrativeAreaLevel1Short: String? = null,
    val country: String? = null,
    val website: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
) : AbstractStore() {
    override fun id() = id
}
