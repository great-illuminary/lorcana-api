package eu.codlab.lorcana.dreamborn.decks

import kotlinx.serialization.Serializable

@Serializable
internal data class CreatorDescriptor(
    val displayName: String,
    val decks: List<DeckDescriptor>,
    val profiles: ProfileDescriptor
) {
    @Serializable
    internal data class ProfileDescriptor(
        val youtube: String? = null
    )
}
