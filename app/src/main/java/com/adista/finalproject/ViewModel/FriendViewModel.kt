package com.adista.finalproject.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.adista.finalproject.data.DataProduct
import com.adista.finalproject.database.Friend
import com.adista.finalproject.repository.FriendRepository
import com.adista.finalproject.repository.ImplDataProductRepo
import com.crocodic.core.base.adapter.CorePagingSource
import com.crocodic.core.base.viewmodel.CoreViewModel
import com.denzcoskun.imageslider.models.SlideModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendViewModel @Inject constructor(
    private val dataProductRepo: ImplDataProductRepo,
    private val friendRepository: FriendRepository
) : CoreViewModel() {

    val queries = MutableStateFlow<Triple<String?, String?, String?>>(Triple(null, null, null))

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getPagingProducts() : Flow<PagingData<DataProduct>> {
        return queries.flatMapLatest {
            Pager(
                config = CorePagingSource.config(10),
                pagingSourceFactory = {
                    CorePagingSource(0) {page: Int, limit: Int ->
                        dataProductRepo.pagingProducts(limit, page * limit ).first()
                    }
                }
            ).flow.cachedIn(viewModelScope)
        }
    }

    private val _slider = MutableSharedFlow<List<SlideModel>>()
    val slider = _slider.asSharedFlow()

    fun getSlider() = viewModelScope.launch {
        dataProductRepo.getSlider().collect{
            val data = ArrayList<SlideModel>()
            it.forEach {photo->
                data.add(SlideModel(photo.thumbnail, photo.title))
            }
            _slider.emit(data)
        }
    }

    
    private val _product = MutableSharedFlow<List<DataProduct>>()
    val product = _product.asSharedFlow()

    fun getProducts(keyword: String = "") = viewModelScope.launch {
        dataProductRepo.getProducts(keyword).collect {
            _product.emit(it)
        }
    }

    fun sortProducts(sortBy: String = "", orderBy: String = "") = viewModelScope.launch {
        dataProductRepo.sortProducts(sortBy, orderBy).collect {
            _product.emit(it)
        }
    }

    fun filterProducts(filter: String = "") = viewModelScope.launch {
        dataProductRepo.filterProducts(filter).collect {
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