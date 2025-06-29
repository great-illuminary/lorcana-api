package eu.codlab.lorcana.rph.sync.user

import eu.codlab.lorcana.rph.sync.database.AppDatabase

interface UserController {
    suspend fun getAll(): List<User>

    suspend fun insert(user: User)

    suspend fun update(user: User)

    suspend fun deleteUsers()
}

internal class UserControllerImpl(database: AppDatabase) : UserController {
    private val dao = database.getUserDao()

    override suspend fun getAll() = dao.getAll()

    override suspend fun insert(user: User) = dao.insert(user)

    override suspend fun update(user: User) = dao.update(user)

    override suspend fun deleteUsers() = dao.deleteUsers()
}
