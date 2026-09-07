package com.whoanimal.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.whoanimal.app.data.local.dao.CardDao
import com.whoanimal.app.data.local.dao.ProfileDao
import com.whoanimal.app.data.local.dao.StorageSlotDao
import com.whoanimal.app.data.local.entities.CardEntity
import com.whoanimal.app.data.local.entities.ProfileEntity
import com.whoanimal.app.data.local.entities.StorageSlotEntity

@Database(
    entities = [
        CardEntity::class,
        StorageSlotEntity::class,
        ProfileEntity::class
    ],
    version = 2,
    exportSchema = false
)

abstract class WhoAnimalDatabase : RoomDatabase() {

    abstract fun cardDao(): CardDao
    abstract fun storageSlotDao(): StorageSlotDao
    abstract fun profileDao(): ProfileDao

    companion object {
        @Volatile
        private var INSTANCE: WhoAnimalDatabase? = null

        fun getInstance(context: Context, databaseName: String = "whoanimal.db"): WhoAnimalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WhoAnimalDatabase::class.java,
                    databaseName
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        fun buildInMemory(context: Context): WhoAnimalDatabase {
            return Room.inMemoryDatabaseBuilder(
                context.applicationContext,
                WhoAnimalDatabase::class.java
            )
                .allowMainThreadQueries()
                .build()
        }

        fun buildPersistent(context: Context, databaseName: String): WhoAnimalDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                WhoAnimalDatabase::class.java,
                databaseName
            )
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
