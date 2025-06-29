package eu.codlab.lorcana.dreamborn.database

import eu.codlab.lorcana.dreamborn.local.Local_creators
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Creator(
    @Transient
    val id: Long = 0,
    val uuid: String,
    val youtube: String? = null,
    val creator: String,
    @Transient
    val tracking: Boolean = false
) {
    companion object {
        internal fun from(localCreator: Local_creators) = Creator(
            id = localCreator.id,
            uuid = localCreator.uuid,
            creator = localCreator.creator_name,
            youtube = localCreator.youtube,
            tracking = localCreator.tracking == 1L
        )
    }
}
