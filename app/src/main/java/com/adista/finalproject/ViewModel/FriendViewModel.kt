package com.adista.finalproject.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adista.finalproject.repository.FriendRepository
import com.adista.finalproject.database.Friend
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendViewModel @Inject constructor(
    private val friendRepository: FriendRepository
) : ViewModel() {

    fun getAllFriends(): LiveData<List<Friend>> {
        return friendRepository.getAllFriends().asLiveData()
    }

    fun getFriendById(friendId: Int): LiveData<Friend?> {
        return friendRepository.getFriendById(friendId)
    }

    fun deleteFriend(friend: Friend) {
        viewModelScope.launch {
            friendRepository.deleteFriend(friend)
        }
    }
}