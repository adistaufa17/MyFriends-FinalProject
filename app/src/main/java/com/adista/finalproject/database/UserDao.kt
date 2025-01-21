package com.adista.finalproject.database

import androidx.room.Dao
import androidx.room.Query
import com.crocodic.core.data.CoreDao

@Dao
interface UserDao : CoreDao<User> {

    @Query("SELECT * FROM user WHERE idDb = 1")
    suspend fun checkLogin(): User?
}