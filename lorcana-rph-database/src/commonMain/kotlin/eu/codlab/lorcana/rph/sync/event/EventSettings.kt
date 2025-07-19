package eu.codlab.lorcana.rph.sync.event

import androidx.room.Entity
import androidx.room.PrimaryKey
import eu.codlab.lorcana.rph.sync.ModelId
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class EventSettings(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val decklistStatus: String,
    val eventLifecycleStatus: String,
    val showRegistrationButton: Boolean,
    val roundDurationInMinutes: Int,
    val paymentInStore: Boolean,
    val paymentOnSpicerack: Boolean,
    val maximumNumberOfGameWinsPerMatch: Int,
    val maximumNumberOfDrawsPerMatch: Int? = null,
    val checkinMethodsCommaSeparated: String,
    val stripePriceId: String?,
    val maximumNumberOfPlayersInMatch: Int?
) : ModelId<Long> {
    override fun modelId() = id
}
