package com.adista.finalproject.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.adista.finalproject.database.Friend
import com.adista.finalproject.database.FriendDao
import com.adista.finalproject.database.FriendDatabase
import kotlinx.coroutines.launch

class FriendViewModel(application: Application) : AndroidViewModel(application) {
    private val friendDao: FriendDao = FriendDatabase.getDatabase(application).friendDao()
    private val allFriends: LiveData<List<Friend>> = friendDao.getAllFriends()

    fun getAllFriends(): LiveData<List<Friend>> {
        return allFriends
    }

    fun getFriendById(friendId: Int): LiveData<Friend?> {
        return friendDao.getFriendById(friendId)
    }

    fun deleteFriend(friend: Friend) {
        viewModelScope.launch {
            friendDao.deleteFriend(friend)
        }
    }
}