package com.adista.finalproject

import androidx.lifecycle.LiveData
import com.adista.finalproject.database.Friend
import kotlinx.coroutines.flow.Flow

interface FriendRepository {

    fun getAllFriends(): Flow<List<Friend>>
    fun getFriendById(id: Int): LiveData<Friend?>
    suspend fun searchFriend(keyword: String?): List<Friend>
    suspend fun deleteFriend(friend: Friend)
    suspend fun updateFriend(friend: Friend)
}