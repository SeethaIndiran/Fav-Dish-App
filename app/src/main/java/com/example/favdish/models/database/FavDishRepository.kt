package com.example.favdish.models.database

import androidx.annotation.WorkerThread
import com.example.favdish.models.entities.FavDish
import kotlinx.coroutines.flow.Flow

class FavDishRepository(private val favDishDao:FavDishDao) {
    @WorkerThread
    suspend fun insertFavDishData(favDish:FavDish){
        favDishDao.insertFavDishDetails(favDish)
    }
    val allDishesList : Flow<List<FavDish>> = favDishDao.getAllDishesList()

    @WorkerThread
    suspend fun updateFavDishData(favDish: FavDish){
        favDishDao.updateFavDishDetails(favDish)
    }

   val favouriteDishes:Flow<List<FavDish>> = favDishDao.getFavouriteDishesList()

    @WorkerThread
    suspend fun deleteFavDishData(favDish:FavDish){
        favDishDao.deleteFavDishDetails(favDish)
    }

    fun filteredListDishes(value: String) : Flow<List<FavDish>> =
        favDishDao.getFilteredDishesList(value)

}