package eu.codlab.lorcana.rph.sync.standings

import androidx.room.Entity

@Entity
data class EventStanding(
    val rank: Int,
    val record: String,
    val matchRecord: String,
    val matchPoints: Int,
    val opponentMatchWinPercentage: Double,
    val opponentGameWinPercentage: Double,

    // foreign keys
    val playerId: Long,
    val userEventStatusId: Long? = null,
)