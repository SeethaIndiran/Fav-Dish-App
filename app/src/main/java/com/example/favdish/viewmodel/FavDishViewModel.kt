package com.example.favdish.viewmodel

import androidx.lifecycle.*
import com.example.favdish.models.database.FavDishRepository
import com.example.favdish.models.entities.FavDish
import kotlinx.coroutines.launch

class FavDishViewModel(private val repository: FavDishRepository):ViewModel() {


    fun insert(dish:FavDish) = viewModelScope.launch{
        repository.insertFavDishData(dish)
    }

    val allDishesList:LiveData<List<FavDish>> = repository.allDishesList.asLiveData()

    fun update(dish:FavDish) = viewModelScope.launch {
        repository.updateFavDishData(dish)
    }

    val favouriteDishes: LiveData<List<FavDish>> = repository.favouriteDishes.asLiveData()

    fun delete(dish:FavDish) = viewModelScope.launch{
        repository.deleteFavDishData(dish)
    }

    fun getFilteredList(value: String): LiveData<List<FavDish>> =
        repository.filteredListDishes(value).asLiveData()

}


class FavDishViewModelFactory(private val repository: FavDishRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(FavDishViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return FavDishViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}