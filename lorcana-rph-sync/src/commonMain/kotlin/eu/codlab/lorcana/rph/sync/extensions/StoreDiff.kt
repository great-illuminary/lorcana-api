package eu.codlab.lorcana.rph.sync.extensions

import eu.codlab.lorcana.rph.store.Store
import eu.codlab.lorcana.rph.store.StoreFull

fun Store.toSyncStore(original: eu.codlab.lorcana.rph.sync.store.Store? = null) =
    eu.codlab.lorcana.rph.sync.store.Store(
        id = id,
        name = name,
        fullAddress = fullAddress,
        administrativeAreaLevel1Short = original?.administrativeAreaLevel1Short,
        country = country,
        website = website,
        latitude = original?.latitude,
        longitude = original?.longitude
    )

fun StoreFull.toSyncStore() =
    eu.codlab.lorcana.rph.sync.store.Store(
        id = id,
        name = name,
        fullAddress = fullAddress,
        administrativeAreaLevel1Short = administrativeAreaLevel1Short,
        country = country,
        website = website,
        latitude = latitude,
        longitude = longitude
    )

fun eu.codlab.lorcana.rph.sync.store.Store.isEquals(other: Store): Boolean {
    if (id != other.id) return false
    if (name != other.name) return false
    if (fullAddress != other.fullAddress) return false
    if (country != other.country) return false
    if (website != other.website) return false

    return true
}

fun eu.codlab.lorcana.rph.sync.store.Store.isEquals(other: StoreFull): Boolean {
    if (id != other.id) return false
    if (name != other.name) return false
    if (fullAddress != other.fullAddress) return false
    if (administrativeAreaLevel1Short != other.administrativeAreaLevel1Short) return false
    if (country != other.country) return false
    if (website != other.website) return false
    if (latitude != other.latitude) return false
    if (longitude != other.longitude) return false

    return true
}

