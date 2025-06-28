package eu.codlab.lorcana.rph.sync

import eu.codlab.lorcana.rph.sync.database.SyncDatabase
import eu.codlab.lorcana.rph.sync.event.Event
import eu.codlab.lorcana.rph.sync.extensions.isEquals
import eu.codlab.lorcana.rph.sync.extensions.toSync
import eu.codlab.lorcana.rph.sync.gameplay.GameplayFormat

internal class GameplayFormatWrapper : AbstractWrapper<GameplayFormat,
        String,
        eu.codlab.lorcana.rph.gameplay.GameplayFormat,
        Unit,
        Unit>() {
    private val gameplayFormats = SyncDatabase.gameplayFormats

    override fun getParentKey(model: GameplayFormat) {
        // nothing
    }

    override suspend fun list() = gameplayFormats.getAll()

    override fun id(fromApi: eu.codlab.lorcana.rph.gameplay.GameplayFormat) = fromApi.id

    override suspend fun insert(copy: GameplayFormat) = gameplayFormats.insert(copy)

    override suspend fun update(copy: GameplayFormat) = gameplayFormats.update(copy)

    override suspend fun toSync(
        fromApi: eu.codlab.lorcana.rph.gameplay.GameplayFormat,
        cached: GameplayFormat?,
        foreignParent: Unit?
    ) = fromApi.toSync()

    override suspend fun isEquals(
        cached: GameplayFormat,
        fromApi: eu.codlab.lorcana.rph.gameplay.GameplayFormat
    ) = cached.isEquals(fromApi)
}