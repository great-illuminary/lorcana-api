package eu.codlab.lorcana.rph.sync.standings

import androidx.room.Entity
import eu.codlab.lorcana.rph.sync.ModelId
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@Entity(primaryKeys = ["roundId", "playerId"])
data class EventStanding(
    val rank: Int,
    val record: String,
    val matchRecord: String,
    val matchPoints: Int,
    val opponentMatchWinPercentage: Double,
    val opponentGameWinPercentage: Double,
    // foreign keys
    @Transient
    val roundId: Long = 0,
    val playerId: Long,
    // val userEventStatusId: Long? = null, -> not a foreign key
) : ModelId<String> {
    override fun modelId() = "${roundId}_$playerId"
}
