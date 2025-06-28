package eu.codlab.lorcana.rph.sync.event

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import eu.codlab.lorcana.rph.sync.ModelId
import kotlinx.serialization.Serializable

@Entity(
    indices = [
        Index(value = ["storeId"]),
        Index(value = ["startDatetimeISO"]),
        Index(value = ["startDatetime"]),
        Index(value = ["endDatetimeISO"]),
        Index(value = ["day2StartDatetimeISO"]),
        Index(value = ["day2StartDatetime"]),
    ]
)
@Serializable
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val fullHeaderImageUrl: String,
    val startDatetimeISO: String? = null,
    val startDatetime: Long? = null,
    val endDatetimeISO: String? = null,
    val day2StartDatetimeISO: String? = null,
    val day2StartDatetime: Long? = null,
    val timerEndDatetime: String? = null,
    val timerPausedAtDatetime: String? = null,
    val timerIsRunning: Boolean,
    val description: String,
    // val tournamentPhases: List<TournamentPhase>, -> computed list
    val registeredUserCount: Int,
    val fullAddress: String,
    val convention: String? = null,
    val displayStatus: String? = null,
    val name: String,
    val pinnedByStore: Boolean,
    val useVerbatimName: Boolean,
    val queueStatus: String,
    val gameType: String,
    val source: String?,
    val eventStatus: String,
    val eventFormat: String,
    val eventType: String,
    val pairingSystem: String?,
    val rulesEnforcementLevel: String,
    val timezone: String? = null,
    val eventAddressOverride: String? = null,
    val eventIsOnline: Boolean,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val costInCents: Double? = null,
    val currency: String,
    val capacity: Int,
    val url: String? = null,
    val numberOfRcInvites: Int? = null,
    val topCutSize: Int? = null,
    val numberOfRounds: Int? = null,
    val numberOfDays: Int? = null,
    val isHeadliningEvent: Boolean,
    val isOnDemand: Boolean,
    val preventSync: Boolean,
    val headerImage: String? = null,
    val startingTableNumber: Int,
    val endingTableNumber: Int? = null,
    val adminListDisplayOrder: Int,
    val prizesAwarded: Boolean,
    val isTestEvent: Boolean,
    val isTemplate: Boolean,
    val taxEnabled: Boolean,
    val polymorphicCtype: Int,
    val game: Int,
    val productList: String? = null,
    val eventFactoryCreatedBy: String?,
    val eventConfigurationTemplate: String,
    val bannerImage: Int,
    val phaseTemplateGroupId: String,

    // foreign keys
    val settingsId: Long,
    val storeId: Long? = null,
    val gameplayFormatId: String,

    // once the event is finished, we can rescan it
    val updatedPostEvent: Boolean,
) : ModelId<Long> {
    override fun modelId() = id
}
