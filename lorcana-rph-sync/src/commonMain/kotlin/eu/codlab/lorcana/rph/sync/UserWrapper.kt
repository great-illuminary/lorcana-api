package eu.codlab.lorcana.rph.sync

import eu.codlab.lorcana.rph.rounds.matches.EventMatchPlayer
import eu.codlab.lorcana.rph.sync.database.SyncDatabase
import eu.codlab.lorcana.rph.sync.extensions.isEquals
import eu.codlab.lorcana.rph.sync.extensions.toSync
import eu.codlab.lorcana.rph.sync.standings.UserEventStatus
import eu.codlab.lorcana.rph.sync.user.User

interface IUserWrapper : CacheAccess<User, Long, Unit> {
    suspend fun matching(matching: String): List<User>
}

internal class UserWrapper : AbstractWrapper<
        User,
        Long,
        EventMatchPlayer,
        UserEventStatus,
        Unit
        >(), IUserWrapper {
    private val stores = SyncDatabase.users

    override fun getParentKey(model: User) {
        // nothing
    }

    override suspend fun list() = stores.getAll()

    override fun id(fromApi: EventMatchPlayer, parent: UserEventStatus?) = fromApi.id

    override suspend fun insert(copy: User) = stores.insert(copy)

    override suspend fun update(copy: User) = stores.update(copy)

    override suspend fun toSync(
        fromApi: EventMatchPlayer,
        cached: User?,
        foreignParent: UserEventStatus?
    ) = fromApi.toSync(foreignParent, cached?.toEventMatchPlayer())

    override suspend fun isEquals(
        cached: User,
        fromApi: EventMatchPlayer,
        parent: UserEventStatus?
    ) = cached.isEquals(fromApi, parent)

    override suspend fun isEquals(cached: User, fromApi: EventMatchPlayer) =
        cached.isEquals(fromApi)

    override suspend fun matching(matching: String) =
        stores.getAll(matching).map { user -> cacheMap[user.id]!! }

    suspend fun fixUsersWithoutProperIdentifier() {
        stores.getAllToFix().forEach {
            cacheMap[it.id]!!.copy(bestIdentifierInGame = it.bestIdentifierInGame).let { copy ->
                println("updating the user ${copy.bestIdentifier}, setting \"${copy.bestIdentifierInGame}\"")
                update(copy)
                updateInternal(copy)
            }
        }
    }
}

internal fun User.toEventMatchPlayer() = EventMatchPlayer(
    id = id, // not used,
    pronouns = pronouns,
    bestIdentifier = bestIdentifier ?: "",
    gameUserProfilePictureUrl = gameUserProfilePictureUrl
)
