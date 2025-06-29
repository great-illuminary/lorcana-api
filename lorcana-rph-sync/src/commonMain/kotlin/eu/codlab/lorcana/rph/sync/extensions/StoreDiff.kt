package eu.codlab.lorcana.rph.sync.extensions

import eu.codlab.lorcana.rph.store.StoreFull

fun StoreFull.toSyncStore(original: eu.codlab.lorcana.rph.sync.store.Store? = null) =
    eu.codlab.lorcana.rph.sync.store.Store(
        id = id,
        name = name,
        fullAddress = fullAddress,
        administrativeAreaLevel1Short = original?.administrativeAreaLevel1Short,
        country = country ?: original?.country,
        website = website ?: original?.website,
        latitude = latitude ?: original?.latitude,
        longitude = longitude ?: original?.longitude
    )

fun eu.codlab.lorcana.rph.sync.store.Store.isEquals(other: StoreFull): Boolean {
    if (id != other.id) return false
    if (name != other.name) return false
    if (fullAddress != other.fullAddress) return false
    if (administrativeAreaLevel1Short != other.administrativeAreaLevel1Short) return false

    // to help on the case where we have a store from the events list, we will skip checking
    other.country?.let { if (country != it) return false }
    other.website?.let { if (website != it) return false }
    other.latitude?.let { if (latitude != it) return false }
    other.longitude?.let { if (longitude != it) return false }

    return true
}
