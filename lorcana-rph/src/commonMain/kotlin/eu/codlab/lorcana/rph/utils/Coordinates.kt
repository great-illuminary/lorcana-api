package eu.codlab.lorcana.rph.utils

import kotlinx.serialization.Serializable

@Serializable
data class Coordinates(
    val type: String,
    val coordinates: List<Double>
)
