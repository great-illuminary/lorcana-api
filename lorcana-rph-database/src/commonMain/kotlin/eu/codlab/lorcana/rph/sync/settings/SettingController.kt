package eu.codlab.lorcana.rph.sync.settings

import eu.codlab.lorcana.rph.sync.database.AppDatabase

interface SettingController {
    suspend fun getAll(): List<Setting>

    suspend fun get(key: String): Setting?

    suspend fun insert(setting: Setting)

    suspend fun update(setting: Setting)

    suspend fun deleteSettings()
}

internal class SettingControllerImpl(database: AppDatabase) : SettingController {
    private val dao = database.getSettingDao()

    override suspend fun getAll() = dao.getAll()

    override suspend fun get(key: String) = dao.get(key)

    override suspend fun insert(setting: Setting) = dao.insert(setting)

    override suspend fun update(setting: Setting) = dao.update(setting)

    override suspend fun deleteSettings() = dao.deleteSettings()
}
