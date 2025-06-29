package eu.codlab.lorcana.rph.sync.match

import androidx.room.Entity
import androidx.room.PrimaryKey
import eu.codlab.lorcana.rph.sync.ModelId
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class EventMatch(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val createdAt: String,
    val updatedAt: String,
    val tableNumber: Int,
    val order: Int? = null,
    val status: String,
    val podNumber: Int? = null,
    val matchIsIntentionalDraw: Boolean,
    val matchIsUnintentionalDraw: Boolean,
    val matchIsBye: Boolean,
    val matchIsLoss: Boolean,
    val reportsAreInConflict: Boolean,
    val gamesDrawn: Int? = null,
    val gamesWonByWinner: Int? = null,
    val gamesWonByLoser: Int? = null,
    val isGhostMatch: Boolean,
    val isFeatureMatch: Boolean,
    val deckCheckStarted: Boolean,
    val deckCheckCompleted: Boolean,
    val timeExtensionSeconds: Int,
    val teamEventMatch: String? = null,
    val tournamentRound: Long,
    val reportingPlayer: Long? = null,
    val winningPlayer: Long? = null,
    val assignedJudge: Long? = null,
    val player1: Long,
    val player1Order: Int? = null,
    val player2: Long? = null,
    val player2Order: Int? = null
) : ModelId<Long> {
    override fun modelId() = id
}