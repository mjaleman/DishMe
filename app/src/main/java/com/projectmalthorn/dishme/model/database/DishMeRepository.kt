package com.projectmalthorn.dishme.model.database

import androidx.annotation.WorkerThread
import com.projectmalthorn.dishme.model.entities.DishMe

class DishMeRepository(private val dishMeDao: DishMeDAO) {

    @WorkerThread
    suspend fun insertDishData(dishMe: DishMe){
        dishMeDao.insertDishDetails(dishMe)
    }
}