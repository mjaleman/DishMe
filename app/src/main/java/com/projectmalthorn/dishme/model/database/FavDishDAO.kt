package com.projectmalthorn.dishme.model.database

import androidx.room.Dao
import androidx.room.Insert
import com.projectmalthorn.dishme.model.entities.FavDish

@Dao
interface FavDishDAO {

    @Insert
    suspend fun insertFavDishDetails(favDish: FavDish)
}