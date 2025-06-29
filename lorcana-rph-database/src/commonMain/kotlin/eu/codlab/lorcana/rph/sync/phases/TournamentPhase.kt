package eu.codlab.lorcana.rph.sync.phases

import androidx.room.Entity
import androidx.room.PrimaryKey
import eu.codlab.lorcana.rph.sync.ModelId
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class TournamentPhase(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val eventId: Long,
    val firstRoundType: String? = null,
    val status: String,
    val orderInPhases: Int,
    val numberOfRounds: Int?,
    val roundType: String,
    val rankRequiredToEnterPhase: Int? = null,
    // val rounds: List<Round>
) : ModelId<Long> {
    override fun modelId() = id
}
