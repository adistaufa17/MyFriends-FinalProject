package com.adista.finalproject.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.adista.finalproject.adapter.FriendAdapter
import com.adista.finalproject.database.Friend
import com.adista.finalproject.ViewModel.FriendViewModel
import com.adista.finalproject.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), FriendAdapter.OnFriendClickListener {

    private lateinit var binding: ActivityMainBinding
    private val friendViewModel: FriendViewModel by viewModels()
    private lateinit var adapter: FriendAdapter
    private var originalData: List<Friend> = emptyList()

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            handlePermissionResults(permissions)
        }

        binding.switchNight.setOnCheckedChangeListener { _, isChecked ->
            switchMode(isChecked)
        }

        checkAndRequestPermissions()

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddFriendActivity::class.java)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(intent)
        }

        adapter = FriendAdapter(this, emptyList(), this)
        binding.rvShowData.adapter = adapter

        friendViewModel.getAllFriends().observe(this) { friends ->
            originalData = friends
            adapter.updateData(friends)
        }

        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isEmpty()) {
                    adapter.updateData(originalData)
                    binding.ivNotFound.visibility = View.GONE
                    binding.rvShowData.visibility = View.VISIBLE
                } else {
                    filterFriends(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun checkAndRequestPermissions() {
        val permissions = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            arrayOf(Manifest.permission.CAMERA)
        } else {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
        }

        val deniedPermissions = permissions.filter { permission ->
            ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED
        }

        if (deniedPermissions.isNotEmpty()) {
            requestPermissionLauncher.launch(deniedPermissions.toTypedArray())
        }
    }

    private fun handlePermissionResults(permissions: Map<String, Boolean>) {
        val deniedPermissions = permissions.filter { !it.value }

        if (deniedPermissions.isEmpty()) {
            Toast.makeText(this, "Semua izin diberikan.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Beberapa izin ditolak: ${deniedPermissions.keys.joinToString()}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun filterFriends(query: String) {
        val filteredList = originalData.filter { it.name.contains(query, ignoreCase = true) }

        if (filteredList.isEmpty()) {
            binding.ivNotFound.visibility = View.VISIBLE
            binding.rvShowData.visibility = View.GONE
            Toast.makeText(this, "Friend Not Found", Toast.LENGTH_SHORT).show()
        } else {
            binding.ivNotFound.visibility = View.GONE
            binding.rvShowData.visibility = View.VISIBLE
            adapter.updateData(filteredList)
        }
    }

    override fun onFriendClick(friendId: Int) {
        val intent = Intent(this, DetailFriendActivity::class.java)
        intent.putExtra("FRIEND_ID", friendId)
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