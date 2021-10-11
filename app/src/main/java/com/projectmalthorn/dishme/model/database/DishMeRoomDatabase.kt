package com.projectmalthorn.dishme.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.projectmalthorn.dishme.model.entities.DishMe

@Database(entities = [DishMe::class], version = 1)
abstract class DishMeRoomDatabase: RoomDatabase() {

    abstract fun dishMeDAO(): DishMeDAO

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: DishMeRoomDatabase? = null

        fun getDatabase(context: Context): DishMeRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DishMeRoomDatabase::class.java,
                    "dish_me_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}

