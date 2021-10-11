package com.projectmalthorn.dishme.model.database

import androidx.room.Dao
import androidx.room.Insert
import com.projectmalthorn.dishme.model.entities.DishMe

@Dao
interface DishMeDAO {

    @Insert
    suspend fun insertFavDishDetails(dishMe: DishMe)
}