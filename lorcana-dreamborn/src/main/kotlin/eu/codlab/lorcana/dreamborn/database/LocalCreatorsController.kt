package eu.codlab.lorcana.dreamborn.database

import eu.codlab.lorcana.dreamborn.local.LocalDatabase

internal class LocalCreatorsController internal constructor(
    private val database: LocalDatabase
) {
    fun selectAllCreators() = database.localCreatorsQueries.selectAll()
        .executeAsList().map { Creator.from(it) }

    fun update(
        id: Long,
        name: String,
        tracking: Boolean,
        youtube: String?
    ) = database.localCreatorsQueries.update(
        creator_name = name,
        tracking = if (tracking) 1L else 0L,
        youtube = youtube,
        id = id
    )

    fun insert(
        uuid: String,
        name: String,
        tracking: Boolean,
        youtube: String?
    ) = database.localCreatorsQueries.insert(
        uuid = uuid,
        creator_name = name,
        tracking = if (tracking) 1L else 0L,
        youtube = youtube
    )
}
