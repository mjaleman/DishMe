package com.projectmalthorn.dishme.application

import android.app.Application
import com.projectmalthorn.dishme.model.database.DishMeRepository
import com.projectmalthorn.dishme.model.database.DishMeRoomDatabase

class DishMeApplication : Application() {

    private val database by lazy{ DishMeRoomDatabase.getDatabase(this@DishMeApplication)}

    val repository by lazy { DishMeRepository(database.dishMeDAO())}
}