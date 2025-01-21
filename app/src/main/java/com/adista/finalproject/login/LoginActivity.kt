package com.adista.finalproject.login

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.adista.finalproject.R
import com.adista.finalproject.activity.MainActivity
import com.adista.finalproject.database.UserDao
import com.adista.finalproject.databinding.ActivityLoginBinding
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.base.activity.CoreActivity
import com.crocodic.core.data.CoreSession
import com.crocodic.core.extension.openActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : CoreActivity<ActivityLoginBinding, LoginViewModel>(R.layout.activity_login) {

    var inputEmail = ""
    var inputPassword = ""

    @Inject
    lateinit var session: CoreSession

    @Inject
    lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.activity = this
        binding.btnLogin.setOnClickListener(this)

        lifecycleScope.launch {
            loadingDialog.show("Check Status")
            if (userDao.checkLogin() != null) {
                openActivity<MainActivity>()
                finish()
            }
            loadingDialog.dismiss()
        }
        observe()
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.apiResponse.collect {
                        if (it.status == ApiStatus.LOADING) {
                            loadingDialog.show("Login")
                        } else {
                            loadingDialog.dismiss()
                        }
                        if (it.status == ApiStatus.SUCCESS) {
                            openActivity<MainActivity>()
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun validateLogin() {
        if (inputEmail.isEmpty()) {
            binding.inputPhone.error = "Isi Email"
            return
        }

        if (inputPassword.isEmpty()) {
            binding.inputPassword.error = "Isi Password"
            return
        }

        viewModel.login(inputEmail, inputPassword)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnLogin -> validateLogin()
        }
    }

    companion object {
        const val EMAIL = "email"
        const val PASS = "password"
       // const val TOKEN = "token"
    }
}