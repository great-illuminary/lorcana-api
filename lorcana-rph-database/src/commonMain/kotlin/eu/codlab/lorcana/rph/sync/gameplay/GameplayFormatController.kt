package eu.codlab.lorcana.rph.sync.gameplay

import eu.codlab.lorcana.rph.sync.database.AppDatabase

interface GameplayFormatController {
    suspend fun getAll(): List<GameplayFormat>

    suspend fun insert(gameplayFormat: GameplayFormat)

    suspend fun update(gameplayFormat: GameplayFormat)

    suspend fun deleteGameplayFormat()
}

internal class GameplayFormatControllerImpl(database: AppDatabase) : GameplayFormatController {
    private val dao = database.getGameplayFormatDao()

    override suspend fun getAll() = dao.getAll()

    override suspend fun insert(gameplayFormat: GameplayFormat) = dao.insert(gameplayFormat)

    override suspend fun update(gameplayFormat: GameplayFormat) = dao.update(gameplayFormat)

    override suspend fun deleteGameplayFormat() = dao.deleteGameplayFormat()
}
