package eu.codlab.lorcana.rph.sync.event

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import eu.codlab.lorcana.rph.sync.ModelId
import korlibs.time.DateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.math.abs

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
    @Transient
    val fullHeaderImageUrl: String = "",
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
    @Transient
    val settingsId: Long = 0,
    @Transient
    val storeId: Long? = null,
    @Transient
    val gameplayFormatId: String = "",
    // once the event is finished, we can rescan it
    @Transient
    val updatedPostEvent: Boolean = false,
    val refreshedAtMilliseconds: Long? = null,
) : ModelId<Long> {
    override fun modelId() = id

    companion object {
        private val day1 = 1000L * 60 * 60 * 24

        // event in the past
        // 2 weeks = 1000ms * 60sec * 60min * 64hour * 15day
        private val week2 = day1 * 15

        private val week1 = day1 * 7
    }

    fun isIn2DaysOrWas2DaysAgo(): Boolean {
        val deltaStart = DateTime.nowUnixMillisLong() - (startDatetime ?: 0L)
        // the event is in the future or 2 days ago/in 2 days
        return abs(deltaStart) < day1 * 2
    }

    fun isInTheFutureAfter2Days(): Boolean {
        val deltaStart = DateTime.nowUnixMillisLong() - (startDatetime ?: 0L)
        // the event is in the future or 2 days ago/in 2 days
        return deltaStart < 0 && !isIn2DaysOrWas2DaysAgo()
    }

    fun fromNthLastWeeks(numberWeeks: Int): Boolean {
        if (isIn2DaysOrWas2DaysAgo() || isInTheFutureAfter2Days()) return false
        val diffLastCheck = DateTime.nowUnixMillisLong() - (startDatetime ?: 0)

        return diffLastCheck < day1 * 7 * numberWeeks && diffLastCheck > day1 * 7 * (numberWeeks - 1)
    }

    @Suppress("ReturnCount")
    fun requiresRefresh(settings: EventSettings): Boolean {
        val shouldCommit = shouldCommitFinalUpdate(settings)

        if (!shouldCommit && eventStatus != "SCHEDULED") return false

        if (null == refreshedAtMilliseconds) {
            return true
        }

        if (startDatetime == null) return false

        val diffLastCheck = DateTime.nowUnixMillisLong() - refreshedAtMilliseconds

        if (fromNthLastWeeks(2)) {
            return false
        }

        if (fromNthLastWeeks(1)) {
            // check that is was 3 days ago we made a check
            return diffLastCheck > day1 * 3
        }

        if (isIn2DaysOrWas2DaysAgo()) {
            // check it's been 20minutes
            return diffLastCheck > 1000L * 60 * 20
        }

        if (isInTheFutureAfter2Days()) {
            // check it's been 1 day
            return diffLastCheck > day1
        }

        return false
    }

    fun shouldCommitFinalUpdate(settings: EventSettings) =
        !updatedPostEvent && settings.eventLifecycleStatus == "EVENT_FINISHED"
}
