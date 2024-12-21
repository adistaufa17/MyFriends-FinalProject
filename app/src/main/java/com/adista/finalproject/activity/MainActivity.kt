package com.adista.finalproject.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.adista.finalproject.R
import com.adista.finalproject.ViewModel.FriendViewModel
import com.adista.finalproject.adapter.FriendAdapter
import com.adista.finalproject.btm_sht.BottomSheetFilterProducts
import com.adista.finalproject.btm_sht.BottomSheetSortingProducts
import com.adista.finalproject.data.DataProduct
import com.adista.finalproject.databinding.ActivityMainBinding
import com.adista.finalproject.databinding.ItemFriendBinding
import com.crocodic.core.base.activity.CoreActivity
import com.crocodic.core.base.adapter.ReactiveListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : CoreActivity<ActivityMainBinding, FriendViewModel>(R.layout.activity_main), FriendAdapter.OnFriendClickListener {

    private val friendViewModel: FriendViewModel by viewModels()
    private lateinit var adapter: FriendAdapter
    private var productList = ArrayList<DataProduct>() // Untuk menyimpan data produk


    private val adapterCore by lazy {
        ReactiveListAdapter<ItemFriendBinding, DataProduct>(R.layout.item_friend)
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

        adapter = FriendAdapter(this, emptyList(), this)
        binding.rvShowData.adapter = adapterCore

        // Panggil getProduct untuk mendapatkan semua produk awal
        friendViewModel.getProducts()

        lifecycleScope.launch {
            friendViewModel.product.collect { data ->
                productList = data as ArrayList<DataProduct> // Simpan data produk
                adapterCore.submitList(data)
            }
        }

        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val keyword = s.toString().trim()
                if (keyword.isEmpty()) {
                    adapterCore.submitList(productList) // Tampilkan semua produk
                    binding.ivNotFound.visibility = View.GONE
                    binding.rvShowData.visibility = View.VISIBLE
                } else {
                    filterProducts(keyword) // Panggil filterProducts dengan keyword
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

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

    private fun filterProducts(query: String) {
        val filteredList = productList.filter { it.title.contains(query, ignoreCase = true) }

        if (filteredList.isEmpty()) {
            binding.ivNotFound.visibility = View.VISIBLE
            binding.rvShowData.visibility = View.GONE
            Toast.makeText(this, "Product Not Found", Toast.LENGTH_SHORT).show()
        } else {
            binding.ivNotFound.visibility = View.GONE
            binding.rvShowData.visibility = View.VISIBLE
            adapterCore.submitList(filteredList) // Update adapter dengan daftar yang difilter
        }
    }

    override fun onFriendClick(itemId: Int) {
        val intent = Intent(this, DetailFriendActivity::class.java)
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