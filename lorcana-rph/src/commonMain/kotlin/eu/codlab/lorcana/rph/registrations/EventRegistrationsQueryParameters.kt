package eu.codlab.lorcana.rph.registrations

import kotlinx.serialization.SerialName
import java.net.URLEncoder

data class EventRegistrationsQueryParameters(
    val page: Int = 1,
    @SerialName("page_size")
    val pageSize: Int = 25
) {
    val toUrl = listOf(
        "page_size" to "$pageSize",
        "page" to "$page",
    ).joinToString("&") { "${it.first}=${URLEncoder.encode(it.second, "UTF-8")}" }
}
