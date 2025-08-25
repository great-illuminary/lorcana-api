package eu.codlab.lorcana.rph.sync.store

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import eu.codlab.lorcana.rph.sync.ModelId
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class Store(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val fullAddress: String,
    val administrativeAreaLevel1Short: String? = null,
    val state: String? = null,
    val country: String? = null,
    val website: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val email: String? = null,
    val streetAddress: String? = null,
    val zipcode: String? = null,
    val phoneNumber: String? = null,
    val storeTypes: String? = null,
    val storeTypesPretty: String? = null,
    @ColumnInfo(
        defaultValue = ""
    )
    val uuid: String = ""
) : ModelId<Long> {
    override fun modelId() = id

    val storeTypesList: List<String>
        get() = storeTypes?.split(",") ?: emptyList()

    val storeTypesPrettyList: List<String>
        get() = storeTypesPretty?.split(",") ?: emptyList()
}
