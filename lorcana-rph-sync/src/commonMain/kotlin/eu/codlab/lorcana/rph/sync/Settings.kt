package eu.codlab.lorcana.rph.sync

import eu.codlab.lorcana.rph.sync.database.SyncDatabase
import eu.codlab.lorcana.rph.sync.settings.Setting

class Settings {
    private val settings = SyncDatabase.settings

    suspend fun lastPage(pageType: PageType) =
        settings.get(pageType.databaseName)?.value?.toIntOrNull() ?: 1

    suspend fun setCurrentPage(pageType: PageType, page: Int) {
        val lastPage = settings.get(pageType.databaseName)

        if (null == lastPage) {
            settings.insert(Setting(key = pageType.databaseName, value = page.toString()))
        } else {
            settings.update(lastPage.copy(value = page.toString()))
        }
    }
}

enum class PageType(val databaseName: String) {
    Events("last_page"),
    Stores("last_page_store");
}