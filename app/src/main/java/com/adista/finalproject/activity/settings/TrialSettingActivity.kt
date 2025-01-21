package com.adista.finalproject.activity.settings

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.adista.finalproject.R
import com.adista.finalproject.databinding.ActivityTrialSettingBinding
import com.adista.finalproject.login.LoginActivity
import com.crocodic.core.base.activity.CoreActivity
import com.crocodic.core.data.CoreSession
import com.crocodic.core.extension.clearNotification
import com.crocodic.core.extension.openActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.crocodic.core.api.ApiStatus
import javax.inject.Inject

@AndroidEntryPoint
class TrialSettingActivity :
    CoreActivity<ActivityTrialSettingBinding, TrialSettingViewModel>(R.layout.activity_trial_setting) {

    @Inject
    lateinit var session: CoreSession
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.apiResponse.collect {
                        if (it.status == ApiStatus.LOADING) {
                            loadingDialog.show("Logout")
                        } else {
                            loadingDialog.dismiss()
                        }
                        if (it.status == ApiStatus.SUCCESS) {
                            loadingDialog.dismiss()
                            expiredDialog.dismiss()
                            clearNotification()
                            openActivity<LoginActivity>()
                            finishAffinity()
                        }
                    }
                }
            }
        }
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
        }
    }
}