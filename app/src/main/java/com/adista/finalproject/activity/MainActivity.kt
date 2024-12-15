package com.adista.finalproject.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.adista.finalproject.adapter.FriendAdapter
import com.adista.finalproject.data.DataProduct
import com.adista.finalproject.ViewModel.FriendViewModel
import com.adista.finalproject.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), FriendAdapter.OnFriendClickListener {

    private lateinit var binding: ActivityMainBinding
    private val friendViewModel: FriendViewModel by viewModels()
    private lateinit var adapter: FriendAdapter
    private var productList = ArrayList<DataProduct>() // Untuk menyimpan data produk

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.switchNight.setOnCheckedChangeListener { _, isChecked ->
            switchMode(isChecked)
        }

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddFriendActivity::class.java)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(intent)
        }

        adapter = FriendAdapter(this, emptyList(), this)
        binding.rvShowData.adapter = adapter

        lifecycleScope.launch {
            friendViewModel.product.collect { products ->
                productList = products as ArrayList<DataProduct>
                adapter.updateData(products)
            }
        }

        friendViewModel.getProduct()

        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isEmpty()) {
                    adapter.updateData(productList)
                    binding.ivNotFound.visibility = View.GONE
                    binding.rvShowData.visibility = View.VISIBLE
                } else {
                    filterProducts(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
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
            adapter.updateData(filteredList)
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