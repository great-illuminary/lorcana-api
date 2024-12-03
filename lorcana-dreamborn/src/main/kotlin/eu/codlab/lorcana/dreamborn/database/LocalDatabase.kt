package eu.codlab.lorcana.dreamborn.database

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver

typealias LocalSqliteDatabase = eu.codlab.lorcana.dreamborn.local.LocalDatabase

internal data class LocalDatabase(
    val localDecksController: LocalDecksController,
    val localCreatorsController: LocalCreatorsController
) {
    companion object {
        fun create(): LocalDatabase {
            val driver = JdbcSqliteDriver("jdbc:sqlite:database.db")
            LocalSqliteDatabase.Schema.create(driver)

            val database = LocalSqliteDatabase(driver)

            return LocalDatabase(
                localDecksController = LocalDecksController(database),
                localCreatorsController = LocalCreatorsController(database)
            )
        }
    }
}
