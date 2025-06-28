package eu.codlab.lorcana.rph.sync

import eu.codlab.lorcana.rph.sync.database.SyncDatabase
import eu.codlab.lorcana.rph.sync.settings.Setting

class Settings {
    private val settings = SyncDatabase.settings

    suspend fun lastPage() = settings.get("last_page")?.value?.toIntOrNull() ?: 1

    suspend fun setCurrentPage(page: Int) {
        val lastPage = settings.get("last_page")

        if (null == lastPage) {
            settings.insert(Setting(key = "last_page", value = page.toString()))
        } else {
            settings.update(lastPage.copy(value = page.toString()))
        }
    }
}