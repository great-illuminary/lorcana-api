package eu.codlab.lorcana.rph.sync

interface CacheAccess<MODEL : ModelId<TYPE>, TYPE, FOREIGN_PARENT_TYPE> {
    fun getFromId(id: TYPE): MODEL?

    fun getCachedList(): List<MODEL>

    fun getFromParent(id: FOREIGN_PARENT_TYPE): List<MODEL>
}