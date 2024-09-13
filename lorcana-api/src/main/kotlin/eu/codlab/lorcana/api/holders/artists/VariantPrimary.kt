package eu.codlab.lorcana.api.holders.artists

import eu.codlab.lorcana.raw.VariantClassification
import eu.codlab.lorcana.raw.VirtualCard
import kotlinx.serialization.Serializable

@Serializable
data class VariantPrimary(
    val variant: VariantClassification,
    val card: VirtualCard
)
