package eu.codlab.lorcana.rph.event

import eu.codlab.lorcana.rph.gameplay.GameplayFormat
import eu.codlab.lorcana.rph.store.AbstractStore
import eu.codlab.lorcana.rph.utils.Coordinates
import korlibs.time.DateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * For instance desribed via
 * https://cf-worker-middleware-hydra-prod.devion-villegas-76b.workers.dev/hydraproxy/api/v2/events/
 */
@Serializable
data class Event<S : AbstractStore>(
    val id: Long,
    @SerialName("full_header_image_url")
    val fullHeaderImageUrl: String,
    @SerialName("start_datetime")
    val startDatetimeISO: String? = null,
    @SerialName("end_datetime")
    val endDatetime: String? = null,
    @SerialName("day_2_start_datetime")
    val day2StartDatetime: String? = null,
    @SerialName("timer_end_datetime")
    val timerEndDatetime: String? = null,
    val timer_paused_at_datetime: String? = null,
    val timer_is_running: Boolean,
    val description: String,
    val settings: EventSettings,
    val tournament_phases: List<TournamentPhase>,
    val registered_user_count: Int,
    val full_address: String,
    val store: S,
    val convention: String? = null,
    @SerialName("gameplay_format")
    val gameplayFormat: GameplayFormat,
    @SerialName("distance_in_miles")
    val distanceInMiles: Int? = null,
    /**
     * Note : seems only available in the list of events() not the event specifically
     */
    @SerialName("display_status")
    val displayStatus: DisplayStatus? = null,
    val name: String,
    @SerialName("pinned_by_store")
    val pinnedByStore: Boolean,
    @SerialName("use_verbatim_name")
    val useVerbatimName: Boolean,
    @SerialName("queue_status")
    val queueStatus: String,
    @SerialName("game_type")
    val gameType: GameType,
    val source: String?,
    @SerialName("event_status")
    val eventStatus: EventStatus,
    @SerialName("event_format")
    val eventFormat: EventFormat,
    @SerialName("event_type")
    val eventType: EventType,
    @SerialName("pairing_system")
    val pairingSystem: String?,
    @SerialName("rules_enforcement_level")
    val rulesEnforcementLevel: RulesEnforcementLevel,
    val coordinates: Coordinates? = null,
    val timezone: String? = null,
    @SerialName("event_address_override")
    val eventAddressOverride: String? = null,
    @SerialName("event_is_online")
    val eventIsOnline: Boolean,
    val latitude: Double? = null,
    val longitude: Double? = null,
    @SerialName("cost_in_cents")
    val costInCents: Double? = null,
    val currency: String,
    val capacity: Int,
    val url: String? = null,
    @SerialName("number_of_rc_invites")
    val numberOfRcInvites: Int? = null,
    @SerialName("top_cut_size")
    val topCutSize: Int? = null,
    @SerialName("number_of_rounds")
    val numberOfRounds: Int? = null,
    @SerialName("number_of_days")
    val numberOfDays: Int? = null,
    @SerialName("is_headlining_event")
    val isHeadliningEvent: Boolean,
    @SerialName("is_on_demand")
    val isOnDemand: Boolean,
    @SerialName("prevent_sync")
    val preventSync: Boolean,
    @SerialName("header_image")
    val headerImage: String? = null,
    @SerialName("starting_table_number")
    val startingTableNumber: Int,
    @SerialName("ending_table_number")
    val endingTableNumber: Int? = null,
    @SerialName("admin_list_display_order")
    val adminListDisplayOrder: Int,
    @SerialName("prizes_awarded")
    val prizesAwarded: Boolean,
    @SerialName("is_test_event")
    val isTestEvent: Boolean,
    @SerialName("is_template")
    val isTemplate: Boolean,
    @SerialName("tax_enabled")
    val taxEnabled: Boolean,
    @SerialName("polymorphic_ctype")
    val polymorphicCtype: Int,
    val game: Int,
    @SerialName("product_list")
    val productList: String? = null,
    @SerialName("event_factory_created_by")
    val eventFactoryCreatedBy: String?,
    @SerialName("event_configuration_template")
    val eventConfigurationTemplate: String,
    @SerialName("banner_image")
    val bannerImage: Int,
    @SerialName("phase_template_group")
    val phaseTemplateGroupId: String
) {
    val startDateTime: DateTime?
        get() = startDatetimeISO?.let { DateTime.fromString(it).utc }
}
