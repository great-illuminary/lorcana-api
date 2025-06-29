package eu.codlab.lorcana.rph.event

import kotlinx.serialization.SerialName
import java.net.URLEncoder

data class EventQueryParameters(
    val startDateAfter: String? = null,
    val page: Int = 1,
    @SerialName("page_size")
    val pageSize: Int = 25
) {
    val toUrl = listOf(
        "start_date_after" to startDateAfter,
        "page_size" to "$pageSize",
        "page" to "$page",
        "game_slug" to "disney-lorcana"
    ).filter { null != it.second }
        .joinToString("&") { "${it.first}=${URLEncoder.encode(it.second, "UTF-8")}" }
}
