package eu.codlab.lorcana.rph.sync.extensions

import eu.codlab.lorcana.rph.store.StoreFull
import eu.codlab.lorcana.rph.store.StoreFullRestLine

fun StoreFull.toSyncStore(
    original: eu.codlab.lorcana.rph.sync.store.Store? = null,
    foreignParent: StoreFullRestLine? = null
) = eu.codlab.lorcana.rph.sync.store.Store(
    id = id,
    name = name,
    fullAddress = fullAddress,
    administrativeAreaLevel1Short = original?.administrativeAreaLevel1Short,
    country = country ?: original?.country,
    website = website ?: original?.website,
    latitude = latitude ?: original?.latitude,
    longitude = longitude ?: original?.longitude,
    // now also copy if required, the information from the fullstore, since those can only
    // be populated when receiving it from the list of stores, we skip if req
    email = email ?: original?.email,
    streetAddress = streetAddress ?: original?.streetAddress,
    zipcode = zipcode ?: original?.zipcode,
    phoneNumber = phoneNumber ?: original?.phoneNumber,
    storeTypes = storeTypes?.joinToString(",") ?: original?.storeTypes,
    storeTypesPretty = storeTypesPretty?.joinToString(",") ?: original?.storeTypesPretty,
    uuid = foreignParent?.id ?: original?.uuid ?: ""
)

fun eu.codlab.lorcana.rph.sync.store.Store.isEquals(
    other: StoreFull,
    foreignParent: StoreFullRestLine? = null
): Boolean {
    if (id != other.id) return false
    if (name != other.name) return false
    if (fullAddress != other.fullAddress) return false
    if (administrativeAreaLevel1Short != other.administrativeAreaLevel1Short) return false

    // to help on the case where we have a store from the events list, we will skip checking
    other.country?.let { if (country != it) return false }
    other.website?.let { if (website != it) return false }
    other.latitude?.let { if (latitude != it) return false }
    other.longitude?.let { if (longitude != it) return false }

    other.email?.let { if (email != it) return false }
    other.streetAddress?.let { if (streetAddress != it) return false }
    other.zipcode?.let { if (zipcode != it) return false }
    other.phoneNumber?.let { if (phoneNumber != it) return false }
    other.storeTypes?.let { if (storeTypes != it.joinToString(",")) return false }
    other.storeTypesPretty?.let { if (storeTypesPretty != it.joinToString(",")) return false }

    foreignParent?.let { if (uuid != it.id) return false }
    return true
}
