package com.adista.finalproject.activity.fcm

import android.annotation.SuppressLint
import android.util.Log
import com.crocodic.core.data.model.AppNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.greenrobot.eventbus.EventBus

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FirebaseMsgService: FirebaseMessagingService() {

    class FirebaseMsgService : FirebaseMessagingService() {

        override fun onNewToken(token: String) {
            super.onNewToken(token)
//        Log.d("fcm-token", "Token: $token")
        }

        override fun onMessageReceived(message: RemoteMessage) {
            super.onMessageReceived(message)

            val inAppNotif = AppNotification(
                title = message.notification?.title,
                content = message.notification?.body
            )

            EventBus.getDefault().post(inAppNotif)
        }
    }
}