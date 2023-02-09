package com.manasmalla.draarogyashealthrecord.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.manasmalla.draarogyashealthrecord.model.Record
import com.manasmalla.draarogyashealthrecord.model.User
import com.manasmalla.draarogyashealthrecord.util.DateConverter
import com.manasmalla.draarogyashealthrecord.util.MetricListConverter

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE user ADD COLUMN is_current_user INTEGER NOT NULL DEFAULT 0")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE record (id INTEGER NOT NULL, record TEXT NOT NULL, date TEXT NOT NULL, userId INTEGER NOT NULL, PRIMARY KEY(id),FOREIGN KEY (userId) REFERENCES user(uId) ON UPDATE NO ACTION ON DELETE CASCADE)")
        database.execSQL("CREATE INDEX index_record_userId ON record (userId)")
    }
}


@Database(entities = [User::class, Record::class], version = 3)
@TypeConverters(MetricListConverter::class, DateConverter::class)
abstract class RecordDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun recordDao(): RecordDao

    companion object {
        @Volatile
        var Instance: RecordDatabase? = null

        fun getDatabase(context: Context): RecordDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, RecordDatabase::class.java, "record_database")
                    .addMigrations(
                        MIGRATION_1_2, MIGRATION_2_3
                    ).build().also {
                        Instance = it
                    }
            }
        }
    }
}