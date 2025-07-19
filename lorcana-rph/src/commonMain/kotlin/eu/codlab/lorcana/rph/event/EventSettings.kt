package eu.codlab.lorcana.rph.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventSettings(
    val id: Long,
    @SerialName("decklist_status")
    val decklistStatus: String,
    @SerialName("event_lifecycle_status")
    val eventLifecycleStatus: String,
    @SerialName("show_registration_button")
    val showRegistrationButton: Boolean,
    @SerialName("round_duration_in_minutes")
    val roundDurationInMinutes: Int,
    @SerialName("payment_in_store")
    val paymentInStore: Boolean,
    @SerialName("payment_on_spicerack")
    val paymentOnSpicerack: Boolean,
    @SerialName("maximum_number_of_game_wins_per_match")
    val maximumNumberOfGameWinsPerMatch: Int,
    @SerialName("maximum_number_of_draws_per_match")
    val maximumNumberOfDrawsPerMatch: Int? = null,
    @SerialName("checkin_methods")
    val checkinMethods: List<String> = emptyList(),
    @SerialName("stripe_price_id")
    val stripePriceId: String?,
    @SerialName("maximum_number_of_players_in_match")
    val maximumNumberOfPlayersInMatch: Int? = null
)
