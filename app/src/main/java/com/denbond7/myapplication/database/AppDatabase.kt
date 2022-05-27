package com.denbond7.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * @author Denis Bondarenko
 *         Date: 5/27/22
 *         Time: 6:22 PM
 *         E-mail: DenBond7@gmail.com
 */
@Database(entities = [ItemEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
    companion object {
        const val DB_NAME = "database.db"
        const val DB_VERSION = 1

        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
