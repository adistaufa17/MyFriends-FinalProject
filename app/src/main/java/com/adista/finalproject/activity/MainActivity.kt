package com.adista.finalproject.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagingData
import com.adista.finalproject.R
import com.adista.finalproject.ViewModel.FriendViewModel
import com.adista.finalproject.adapter.FriendAdapter
import com.adista.finalproject.btm_sht.BottomSheetFilterProducts
import com.adista.finalproject.btm_sht.BottomSheetSortingProducts
import com.adista.finalproject.data.DataProduct
import com.adista.finalproject.databinding.ActivityMainBinding
import com.adista.finalproject.databinding.ItemFriendBinding
import com.crocodic.core.base.activity.CoreActivity
import com.crocodic.core.base.adapter.PaginationAdapter
import com.crocodic.core.extension.openActivity
import com.crocodic.core.extension.toJson
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : CoreActivity<ActivityMainBinding, FriendViewModel>(R.layout.activity_main), FriendAdapter.OnFriendClickListener {

    private val friendViewModel: FriendViewModel by viewModels()
    private var productList = ArrayList<DataProduct>()

    private lateinit var adapter: FriendAdapter

    @Inject
    lateinit var gson: Gson

    private val adapterCore by lazy {
        PaginationAdapter<ItemFriendBinding, DataProduct>(R.layout.item_friend).initItem { _, data ->
            openActivity<DetailProductActivity> {
                val dataProduct = data.toJson(gson)
                putExtra(DetailProductActivity.DATA, dataProduct)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.switchNight.setOnCheckedChangeListener { _, isChecked ->
            switchMode(isChecked)
        }

       // adapter = FriendAdapter(this, emptyList(), this)
        binding.rvShowData.adapter = adapterCore

        lifecycleScope.launch {
            friendViewModel.queries.emit(Triple("", "", ""))
        }
        friendViewModel.getSlider()


        binding.searchBar.doOnTextChanged { text, _, _, _ ->
            val keyword = "%${text.toString().trim()}%"
//            viewModel.getFriend(keyword)
            friendViewModel.getProducts(keyword)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
                    friendViewModel.slider.collectLatest { data ->
                        binding.ivSlider.setImageList(data)
                    }
                }

                launch {
                    friendViewModel.getPagingProducts().collectLatest { data : PagingData<DataProduct> ->
                        //productList = data as ArrayList<DataProduct> // Simpan data produk
                        adapterCore.submitData(data)
                    }
                }
            }
        }

        binding.fbtnFilter.setOnClickListener {
            val btmSht = BottomSheetFilterProducts { filter ->
                friendViewModel.filterProducts(filter)
            }
            btmSht.show(supportFragmentManager, "BtmShtFilteringProducts")
        }

        binding.fbtnSort.setOnClickListener {
            val btmSht = BottomSheetSortingProducts { sortBy, order ->
                friendViewModel.sortProducts(sortBy, order)
            }
            btmSht.show(supportFragmentManager, "BtmShtSortingProducts")
        }
    }

//    private fun filterProducts(query: String) {
//        val filteredList = productList.filter { it.title.contains(query, ignoreCase = true) }
//
//        if (filteredList.isEmpty()) {
//            binding.ivNotFound.visibility = View.VISIBLE
//            binding.rvShowData.visibility = View.GONE
//            Toast.makeText(this, "Product Not Found", Toast.LENGTH_SHORT).show()
//        } else {
//            binding.ivNotFound.visibility = View.GONE
//            binding.rvShowData.visibility = View.VISIBLE
//            adapterCore.submitList(filteredList) // Update adapter dengan daftar yang difilter
//        }
//    }

    override fun onFriendClick(itemId: Int) {
        val intent = Intent(this, DetailProductActivity::class.java)
        intent.putExtra("PRODUCT_ID", itemId)
        startActivity(intent)
    }

    private fun switchMode(isChecked: Boolean) {
        if (isChecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            Toast.makeText(this, "Night Mode Enabled", Toast.LENGTH_SHORT).show()
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Toast.makeText(this, "Day Mode Enabled", Toast.LENGTH_SHORT).show()
        }
    }
}