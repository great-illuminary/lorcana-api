package eu.codlab.lorcana.rph.sync.round

import androidx.room.Entity
import androidx.room.PrimaryKey
import eu.codlab.lorcana.rph.sync.ModelId
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class Round(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val tournamentPhaseId: Long,
    val roundNumber: Int,
    val finalRoundInEvent: Boolean,
    val pairingsStatus: String,
    val standingsStatus: String,
    val roundType: String,
    val status: String,
) : ModelId<Long> {
    override fun modelId() = id
}
