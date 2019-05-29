package com.sdmadsen.steve.lawntracker

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@Database(entities = arrayOf(Mow::class, TimeLog::class), version = 1)
@TypeConverters(Converters::class)
public abstract class MowRoomDatabase : RoomDatabase() {

    abstract fun mowDao(): MowDao
    abstract fun timeLogDao(): TimeLogDao

    companion object {
        @Volatile
        private var INSTANCE: MowRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): MowRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MowRoomDatabase::class.java,
                    "Mow_database"
                )
                    .addCallback(MowDatabaseCallback(scope)).build()
                INSTANCE = instance
                return instance
            }
        }

        private class MowDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        //populateDatabase(database.mowDao())
                    }
                }
            }
        }
    }
}