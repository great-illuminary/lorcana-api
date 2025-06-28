package eu.codlab.lorcana.rph.sync.gameplay

import androidx.room.Entity
import androidx.room.PrimaryKey
import eu.codlab.lorcana.rph.sync.ModelId
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class GameplayFormat(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String? = null
) : ModelId<String> {
    override fun modelId() = id
}