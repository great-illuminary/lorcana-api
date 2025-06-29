package eu.codlab.lorcana.rph.utils

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Page<T>(
    @SerialName("page_size")
    val pageSize: Int,
    val count: Int,
    val total: Int,
    @SerialName("current_page_number")
    val currentPageNumber: Int,
    @SerialName("next_page_number")
    val nextPageNumber: Int? = null,
    val next: Int? = null,
    val previous: Int? = null,
    @SerialName("previous_page_number")
    val previousPageNumber: Int? = null,
    val results: List<T>
)
