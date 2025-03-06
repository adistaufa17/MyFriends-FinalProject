package com.adista.finalproject.activity.push_notification

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.adista.finalproject.R
import com.adista.finalproject.databinding.ActivityPushNotificationBinding
import com.crocodic.core.base.activity.NoViewModelActivity
import com.google.firebase.messaging.FirebaseMessaging

class PushNotificationActivity :
    NoViewModelActivity<ActivityPushNotificationBinding>(R.layout.activity_push_notification) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getFcmToken()
    }

    private fun getFcmToken() {
        generateFirebaseToken {FcmToken ->
            Log.d("firebase-token", FcmToken)
        }
    }

    private fun generateFirebaseToken(result: (String) -> Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            result(it.result)
        }
    }
}