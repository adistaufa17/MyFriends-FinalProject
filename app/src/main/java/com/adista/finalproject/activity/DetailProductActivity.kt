package com.adista.finalproject.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.adista.finalproject.R
import com.adista.finalproject.data.DataProduct
import com.adista.finalproject.databinding.ActivityDetailProductBinding
import com.crocodic.core.base.activity.NoViewModelActivity
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailProductActivity :
    NoViewModelActivity<ActivityDetailProductBinding>(R.layout.activity_detail_product) {

    @Inject
    lateinit var gson: Gson

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val dataIntent = intent.getStringExtra(DATA)
        binding.data = gson.fromJson(dataIntent, DataProduct::class.java)

    }

    companion object {
        const val DATA = "data"
    }
}