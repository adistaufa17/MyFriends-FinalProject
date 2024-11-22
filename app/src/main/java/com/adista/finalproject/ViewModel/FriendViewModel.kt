package com.adista.finalproject.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.ViewModel // Ensure you import ViewModel from androidx.lifecycle
import androidx.lifecycle.viewModelScope
import com.adista.finalproject.repository.FriendRepository
import com.adista.finalproject.database.Friend
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendViewModel @Inject constructor(
    private val friendRepository: FriendRepository
) : ViewModel() { // Change this to extend ViewModel

    // Fetch all friends
    fun getAllFriends(): LiveData<List<Friend>> {
        return friendRepository.getAllFriends().asLiveData()
    }

    // Fetch friend by ID
    fun getFriendById(friendId: Int): LiveData<Friend?> {
        return friendRepository.getFriendById(friendId)
    }

    // Delete a friend
    fun deleteFriend(friend: Friend) {
        viewModelScope.launch {
            friendRepository.deleteFriend(friend)
        }
    }

    // Update a friend
    fun updateFriend(friend: Friend) {
        viewModelScope.launch {
            friendRepository.updateFriend(friend)
        }
    }

    fun apiLogout() {}
    fun apiRenewToken() {}
}