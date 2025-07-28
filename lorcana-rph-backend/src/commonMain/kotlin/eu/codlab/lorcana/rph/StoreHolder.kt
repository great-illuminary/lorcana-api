package eu.codlab.lorcana.rph

import eu.codlab.lorcana.rph.sync.store.Store
import kotlinx.serialization.Serializable

@Serializable
data class StoreHolder(
    val id: Long,
    val uuid: String,
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

fun Store.toStoreHolder() = StoreHolder(
    id = id,
    uuid = uuid,
    name = name,
    fullAddress = fullAddress,
    administrativeAreaLevel1Short = administrativeAreaLevel1Short,
    country = country,
    website = website,
    latitude = latitude,
    longitude = longitude,
    email = email,
    streetAddress = streetAddress,
    zipcode = zipcode,
    phoneNumber = phoneNumber,
    storeTypes = storeTypesList,
    storeTypesPretty = storeTypesPrettyList
)
