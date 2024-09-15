package com.example.favdish.models.application

import android.app.Application
import com.example.favdish.models.database.FavDishRepository
import com.example.favdish.models.database.FavDishRoomDatabase

class FavDishApplication:Application() {

    private val database by lazy{
        FavDishRoomDatabase.getDatabase((this@FavDishApplication))
    }
    val repository by lazy{
        FavDishRepository(database.favDishDao())
    }
}