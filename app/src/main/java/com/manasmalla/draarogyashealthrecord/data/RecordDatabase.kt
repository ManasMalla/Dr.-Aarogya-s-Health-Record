package com.manasmalla.draarogyashealthrecord.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.manasmalla.draarogyashealthrecord.model.User
import com.manasmalla.draarogyashealthrecord.util.MetricListConverter

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE user ADD COLUMN is_current_user INTEGER NOT NULL DEFAULT 0")
    }
}

@Database(entities = [User::class], version = 2)
@TypeConverters(MetricListConverter::class)
abstract class RecordDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        var Instance: RecordDatabase? = null

        fun getDatabase(context: Context): RecordDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, RecordDatabase::class.java, "record_database")
                    .addMigrations(
                        MIGRATION_1_2
                    ).build().also {
                        Instance = it
                    }
            }
        }
    }
}