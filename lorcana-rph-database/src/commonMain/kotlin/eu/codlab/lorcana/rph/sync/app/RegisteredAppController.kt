package eu.codlab.lorcana.rph.sync.app

import eu.codlab.lorcana.rph.sync.database.AppDatabase

/*interface RegisteredAppController {
    suspend fun getAll(): List<RegisteredApp>

    suspend fun getIdForToken(appToken: String): Long

    suspend fun get(appToken: String): RegisteredApp

    suspend fun insert(registeredApp: RegisteredApp)

    suspend fun update(registeredApp: RegisteredApp)

    suspend fun deleteRegisteredApps()
}

internal class RegisteredAppControllerImpl(database: AppDatabase) : RegisteredAppController {
    // private val dao = database.getRegisteredApp()

    override suspend fun getAll() = dao.getAll()

    override suspend fun getIdForToken(appToken: String) = dao.getIdForToken(appToken)

    override suspend fun get(appToken: String) = dao.get(appToken)

    override suspend fun insert(registeredApp: RegisteredApp) = dao.insert(registeredApp)

    override suspend fun update(registeredApp: RegisteredApp) = dao.update(registeredApp)

    override suspend fun deleteRegisteredApps() = dao.deleteRegisteredApps()
}
*/