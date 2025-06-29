package eu.codlab.lorcana.rph.sync.database

import androidx.room.Room
import androidx.room.RoomDatabase
import eu.codlab.files.VirtualFile

internal actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = VirtualFile(VirtualFile.Root, "database-ravensburger.db")

    println("will create database at ${dbFile.absolutePath}")

    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath,
    )
}
