package com.example.favdish.models.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.favdish.models.entities.FavDish

@Database(entities = [FavDish::class], version = 1)
abstract class FavDishRoomDatabase : RoomDatabase(){

    abstract fun favDishDao():FavDishDao

    companion object{
        @Volatile
        private var INSTANCE:FavDishRoomDatabase? = null

        fun getDatabase(context: Context):FavDishRoomDatabase{
            // if the INSTANCE is not null,return it,
            // if it is, then create the database

            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavDishRoomDatabase::class.java,
                    "fav_dish_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                //return instance
                instance
            }

        }
    }
}