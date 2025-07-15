package eu.codlab.lorcana.rph.store

import kotlinx.serialization.SerialName
import java.net.URLEncoder

data class StoresQueryParameters(
    val latitude: Double? = null,
    val longitude: Double? = null,
    @SerialName("num_miles")
    val miles: Int? = null,
    @SerialName("game_id")
    val gameId: Int = 1,
    val page: Int = 1,
    @SerialName("page_size")
    val pageSize: Int = 25
) {
    val toUrl = listOf(
        "page_size" to "$pageSize",
        "page" to "$page",
        "latitude" to latitude?.toString(),
        "longitude" to longitude?.toString(),
        "miles" to miles?.toString(),
        "game_id" to "$gameId",
    ).filter { it.second != null }
        .joinToString("&") { "${it.first}=${URLEncoder.encode(it.second, "UTF-8")}" }
}