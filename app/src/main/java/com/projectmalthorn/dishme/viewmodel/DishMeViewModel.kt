package com.projectmalthorn.dishme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.projectmalthorn.dishme.model.database.DishMeRepository
import com.projectmalthorn.dishme.model.entities.DishMe
import kotlinx.coroutines.launch

class DishMeViewModel(private val repository: DishMeRepository) : ViewModel() {

    fun insert(dish: DishMe) = viewModelScope.launch {
        repository.insertDishData(dish)
    }

}

class DishMeViewModelFactory(private val repository: DishMeRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DishMeViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return DishMeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}