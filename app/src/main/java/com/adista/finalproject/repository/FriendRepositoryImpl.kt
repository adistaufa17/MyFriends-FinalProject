package com.adista.finalproject.repository

import androidx.lifecycle.LiveData
import com.adista.finalproject.database.Friend
import com.adista.finalproject.database.FriendDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FriendRepositoryImpl @Inject constructor(
    private val friendDao: FriendDao
) : FriendRepository {

    override fun getAllFriends(): Flow<List<Friend>> {
        return friendDao.getAllFriends()
    }

    override fun getFriendById(id: Int): LiveData<Friend?> {
        return friendDao.getFriendById(id)
    }

    override suspend fun searchFriend(keyword: String?): List<Friend> {
        return friendDao.searchFriend(keyword)
    }

    override suspend fun deleteFriend(friend: Friend) {
        friendDao.deleteFriend(friend)
    }

    override suspend fun updateFriend(friend: Friend) {
        friendDao.updateFriend(friend)
    }
}