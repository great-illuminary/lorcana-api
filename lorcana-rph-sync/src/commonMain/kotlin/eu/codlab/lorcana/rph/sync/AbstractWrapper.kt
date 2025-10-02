package eu.codlab.lorcana.rph.sync

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@Suppress("TooManyFunctions")
internal sealed class AbstractWrapper<
        MODEL : ModelId<TYPE>,
        TYPE,
        FROM_API,
        FOREIGN_PARENT,
        FOREIGN_PARENT_TYPE
        > : CacheAccess<MODEL, TYPE, FOREIGN_PARENT_TYPE> {
    protected val scope = CoroutineScope(Dispatchers.IO)

    protected val cache = mutableListOf<MODEL>()
    protected val cacheMap = mutableMapOf<TYPE, MODEL>()
    protected val parentMap = mutableMapOf<FOREIGN_PARENT_TYPE, MutableList<MODEL>>()

    final override fun getFromParent(id: FOREIGN_PARENT_TYPE) = parentMap[id] ?: emptyList()

    suspend fun initialize() {

        list().let { list ->
            if(this is UserWrapper) {
                println("UserWrapper initializing -> ${list.size}")
            }

            cache.addAll(list)
            list.forEach {
                cacheMap[it.modelId()] = it

                attemptToPutInCacheForParent(it)
            }
        }
    }

    private fun attemptToPutInCacheForParent(model: MODEL) {
        val parentId = getParentKey(model)

        if (parentId is Unit) return

        parentMap.computeIfAbsent(parentId) { mutableListOf() }
        parentMap[parentId]!!.add(model)
    }

    fun asCacheAccess(): CacheAccess<MODEL, TYPE, FOREIGN_PARENT_TYPE> = this

    protected abstract fun getParentKey(model: MODEL): FOREIGN_PARENT_TYPE

    protected abstract suspend fun list(): List<MODEL>

    protected open suspend fun isEquals(
        cached: MODEL,
        fromApi: FROM_API,
        parent: FOREIGN_PARENT? = null
    ) = isEquals(cached, fromApi)

    protected abstract suspend fun isEquals(cached: MODEL, fromApi: FROM_API): Boolean

    protected abstract suspend fun toSync(
        fromApi: FROM_API,
        cached: MODEL?,
        foreignParent: FOREIGN_PARENT? = null
    ): MODEL

    protected abstract suspend fun update(copy: MODEL)

    protected abstract suspend fun insert(copy: MODEL)

    override fun getFromId(id: TYPE) = cacheMap[id]

    override fun getCachedList(): List<MODEL> = cache

    abstract fun id(fromApi: FROM_API, parent: FOREIGN_PARENT?): TYPE

    protected fun updateInternal(copy: MODEL) {
        cache.indexOfFirst { it.modelId() == copy.modelId() }.let { existingId ->
            if (existingId >= 0) {
                cache[existingId] = copy
            } else {
                cache.add(copy)
            }
        }

        cacheMap[copy.modelId()] = copy
    }

    suspend fun check(
        fromApi: FROM_API,
        foreignParent: FOREIGN_PARENT? = null
    ): GenerationModel<MODEL> {
        val id = id(fromApi, foreignParent)
        println("checking adding $id to the database...")
        // doesn't check the tournament phase
        // nor the rounds just yet

        val cached = cacheMap[id]

        if (null == cached || !isEquals(cached, fromApi, foreignParent)) {
            val copy = toSync(fromApi, cached, foreignParent)

            updateInternal(copy)

            if (null == cached) {
                insert(copy)

                attemptToPutInCacheForParent(copy)
            } else {
                update(copy)
            }
            return GeneratedModel(previous = cached, new = copy)
        } else {
            return PreviousModel(previous = cached)
        }
    }
}

sealed class GenerationModel<MODEL>

internal data class GeneratedModel<MODEL>(
    val previous: MODEL?,
    val new: MODEL
) : GenerationModel<MODEL>()

internal data class PreviousModel<MODEL>(
    val previous: MODEL
) : GenerationModel<MODEL>()
