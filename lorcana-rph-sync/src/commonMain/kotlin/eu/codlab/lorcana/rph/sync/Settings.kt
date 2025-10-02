package eu.codlab.lorcana.rph.sync

import eu.codlab.lorcana.rph.sync.database.SyncDatabase
import eu.codlab.lorcana.rph.sync.settings.Setting

class Settings {
    private val wrapped = SyncDatabase.settings

    suspend fun lastPage(pageType: PageType) =
        wrapped.get(pageType.databaseName)?.value?.toIntOrNull() ?: 1

    suspend fun setCurrentPage(pageType: PageType, page: Int) {
        val lastPage = wrapped.get(pageType.databaseName)

        if (null == lastPage) {
            wrapped.insert(Setting(key = pageType.databaseName, value = page.toString()))
        } else {
            wrapped.update(lastPage.copy(value = page.toString()))
        }
    }

    suspend fun needFullSync(): Boolean =
        wrapped.get("full_sync")?.value?.toBooleanStrictOrNull()?.let { !it } ?: true

    suspend fun commitFullSync() {
        val fullSync = wrapped.get("full_sync")

        if (null == fullSync) {
            wrapped.insert(Setting(key = "fullSync", value = "true"))
        } else {
            wrapped.update(fullSync.copy(value = "true"))
        }
    }
}

enum class PageType(val databaseName: String) {
    Stores("last_page_store")
}
