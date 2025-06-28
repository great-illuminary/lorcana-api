package eu.codlab.lorcana.rph.sync.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import eu.codlab.lorcana.rph.sync.event.Event
import eu.codlab.lorcana.rph.sync.event.EventDao
import eu.codlab.lorcana.rph.sync.event.EventSettings
import eu.codlab.lorcana.rph.sync.event.EventSettingsDao
import eu.codlab.lorcana.rph.sync.gameplay.GameplayFormat
import eu.codlab.lorcana.rph.sync.gameplay.GameplayFormatDao
import eu.codlab.lorcana.rph.sync.phases.TournamentPhase
import eu.codlab.lorcana.rph.sync.phases.TournamentPhaseDao
import eu.codlab.lorcana.rph.sync.round.Round
import eu.codlab.lorcana.rph.sync.round.RoundDao
import eu.codlab.lorcana.rph.sync.settings.Setting
import eu.codlab.lorcana.rph.sync.settings.SettingDao
import eu.codlab.lorcana.rph.sync.standings.EventStandingDao
import eu.codlab.lorcana.rph.sync.standings.UserEventStatusDao
import eu.codlab.lorcana.rph.sync.store.Store
import eu.codlab.lorcana.rph.sync.store.StoreDao
import eu.codlab.lorcana.rph.sync.user.User
import eu.codlab.lorcana.rph.sync.user.UserDao
import kotlinx.coroutines.Dispatchers

@Database(
    entities = [
        Store::class,
        User::class,
        Event::class,
        EventSettings::class,
        GameplayFormat::class,
        TournamentPhase::class,
        Round::class,
        Setting::class
    ], version = 1
)
@ConstructedBy(AppDatabaseConstructor::class)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun getStoreDao(): StoreDao

    abstract fun getUserDao(): UserDao

    abstract fun getGameplayFormatDao(): GameplayFormatDao

    abstract fun getEventSettingsDao(): EventSettingsDao

    abstract fun getEventDao(): EventDao

    abstract fun getRoundDao(): RoundDao

    abstract fun getTournamentPhaseDao(): TournamentPhaseDao

    abstract fun getEventStandings(): EventStandingDao

    abstract fun getUserEventStatus(): UserEventStatusDao

    abstract fun getSettingDao(): SettingDao
}

internal expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

internal expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>

internal fun getDatabase() = getDatabaseBuilder()
    // .addMigrations(MIGRATIONS)
    // .fallbackToDestructiveMigrationOnDowngrade()
    .setDriver(BundledSQLiteDriver())
    .setQueryCoroutineContext(Dispatchers.IO)
    .build()