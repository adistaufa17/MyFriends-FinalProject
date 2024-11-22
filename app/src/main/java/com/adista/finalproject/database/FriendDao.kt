package com.adista.finalproject.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao // Make sure this annotation is present.
interface FriendDao {

    @Query("SELECT * FROM friends") // Adjust table name as necessary.
    fun getAllFriends(): Flow<List<Friend>>

    @Query("SELECT * FROM friends WHERE id = :id")
    fun getFriendById(id: Int): LiveData<Friend?>

    @Query("SELECT * FROM friends WHERE name LIKE :keyword")
    fun searchFriend(keyword: String?): List<Friend>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFriend(friend: Friend)

    @Update
    suspend fun updateFriend(friend: Friend)

    @Delete
    suspend fun deleteFriend(friend: Friend)
}