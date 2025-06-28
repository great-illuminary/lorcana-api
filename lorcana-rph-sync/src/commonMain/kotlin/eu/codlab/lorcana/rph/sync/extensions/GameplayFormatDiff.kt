package eu.codlab.lorcana.rph.sync.extensions

import eu.codlab.lorcana.rph.gameplay.GameplayFormat

fun GameplayFormat.toSync() =
    eu.codlab.lorcana.rph.sync.gameplay.GameplayFormat(
        id = id,
        name = name,
        description = description
    )

fun eu.codlab.lorcana.rph.sync.gameplay.GameplayFormat.isEquals(other: GameplayFormat): Boolean {
    if (id != other.id) return false
    if (name != other.name) return false
    if (description != other.description) return false

    return true
}

