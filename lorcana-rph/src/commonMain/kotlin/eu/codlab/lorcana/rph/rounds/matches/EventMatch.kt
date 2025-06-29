package eu.codlab.lorcana.rph.rounds.matches

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventMatch(
    val id: Long,
    @SerialName("player_match_relationships")
    val playerMatchRelationships: List<EventPlayerMatchRelationship>,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("table_number")
    val tableNumber: Int,
    val order: Int? = null,
    val status: String,
    @SerialName("pod_number")
    val podNumber: Int? = null,
    @SerialName("match_is_intentional_draw")
    val matchIsIntentionalDraw: Boolean,
    @SerialName("match_is_unintentional_draw")
    val matchIsUnintentionalDraw: Boolean,
    @SerialName("match_is_bye")
    val matchIsBye: Boolean,
    @SerialName("match_is_loss")
    val matchIsLoss: Boolean,
    @SerialName("reports_are_in_conflict")
    val reportsAreInConflict: Boolean,
    @SerialName("games_drawn")
    val gamesDrawn: Int? = null,
    @SerialName("games_won_by_winner")
    val gamesWonByWinner: Int? = null,
    @SerialName("games_won_by_loser")
    val gamesWonByLoser: Int? = null,
    @SerialName("is_ghost_match")
    val isGhostMatch: Boolean,
    @SerialName("is_feature_match")
    val isFeatureMatch: Boolean,
    @SerialName("deck_check_started")
    val deckCheckStarted: Boolean,
    @SerialName("deck_check_completed")
    val deckCheckCompleted: Boolean,
    @SerialName("time_extension_seconds")
    val timeExtensionSeconds: Int,
    @SerialName("team_event_match")
    val teamEventMatch: String? = null,
    @SerialName("tournament_round")
    val tournamentRound: Long,
    @SerialName("reporting_player")
    val reportingPlayer: Long? = null,
    @SerialName("winning_player")
    val winningPlayer: Long? = null,
    @SerialName("assigned_judge")
    val assignedJudge: Long? = null,
    val players: List<Long>
) {
    fun player1() = players.first()

    fun player2() = players.getOrNull(1)

    fun player1Order() =
        this.playerMatchRelationships.first { it.player.id == player1() }.playerOrder

    fun player2Order() =
        this.playerMatchRelationships.firstOrNull { it.player.id == player2() }?.playerOrder
}
