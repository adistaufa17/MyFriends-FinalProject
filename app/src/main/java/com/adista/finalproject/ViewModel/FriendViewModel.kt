package com.adista.finalproject.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.adista.finalproject.data.DataProduct
import com.adista.finalproject.repository.FriendRepository
import com.adista.finalproject.database.Friend
import com.adista.finalproject.repository.ImplDataProductRepo
import com.crocodic.core.base.viewmodel.CoreViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendViewModel @Inject constructor(
    private val dataProductRepo: ImplDataProductRepo,
    private val friendRepository: FriendRepository
) : CoreViewModel() {

    private val _product = MutableSharedFlow<List<DataProduct>>()
    val product = _product.asSharedFlow()

    fun getProduct(keyword: String = "") = viewModelScope.launch {
        dataProductRepo.getProducts(keyword).collect {
            _product.emit(it)
        }
    }


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

    override fun apiLogout() {
    }

    override fun apiRenewToken() {
    }
}