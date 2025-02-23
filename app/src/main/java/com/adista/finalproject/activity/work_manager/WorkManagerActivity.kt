package com.adista.finalproject.activity.work_manager

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.adista.finalproject.R
import com.adista.finalproject.activity.work_manager.worker.InAppNotificationWorker
import com.adista.finalproject.activity.work_manager.worker.NotificationWorker
import com.adista.finalproject.databinding.ActivityWorkManagerBinding
import com.crocodic.core.base.activity.NoViewModelActivity
import com.crocodic.core.extension.snacked
import com.crocodic.core.extension.text
import com.crocodic.core.extension.tos
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class WorkManagerActivity :
    NoViewModelActivity<ActivityWorkManagerBinding>(R.layout.activity_work_manager) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkNotificationPermission()
        }


        binding.btnShow.setOnClickListener {
            showNotification("Halo!", "Saya sedang belajar membuat notifikasi di Aplikasi")
        }

        binding.btnRequest.setOnClickListener {
            createWorkNotification()
        }

        binding.btnInAppNotification.setOnClickListener {
            createInAppNotification()
        }
    }

    private fun createInAppNotification() {
        val inputText = binding.etCount.text.toString().trim() // Ambil input dan hapus spasi
        if (inputText.isEmpty()) {
            // Tampilkan pesan kesalahan jika input kosong
            Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show()
            return
        }

        val inputSecond = inputText.toLong() // Konversi ke Long setelah pemeriksaan

        val notificationWorkRequest: WorkRequest =
            OneTimeWorkRequestBuilder<InAppNotificationWorker>()
                .setInitialDelay(inputSecond, TimeUnit.SECONDS)
                .build()

        WorkManager.getInstance(this).enqueue(notificationWorkRequest)

        showCountDown(inputSecond)
    }

    private fun showCountDown(countdown: Long) {
        object : CountDownTimer(TimeUnit.SECONDS.toMillis(countdown), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvCountdown.text("${millisUntilFinished / 1000}")
            }

            override fun onFinish() {}
        }.start()
    }


    private fun createWorkNotification() {
        val notificationWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(10, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(this).enqueue(notificationWorkRequest)
        tos("Aplikasi akan tertutup dan tunggu 10 detik ya")

        finishAffinity()
    }

    private fun showNotification(title: String, content: String) {
        createNotificationChannel()

        val intent = Intent(this, WorkManagerActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle(title)
            .setContentText(content)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@WorkManagerActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            notify(1, builder.build())
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = CHANNEL_DESCRIPTION
        }

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkNotificationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
            }

            shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected, and what
                // features are disabled if it's declined. In this UI, include a
                // "cancel" or "no thanks" button that lets the user continue
                // using your app without granting the permission.
                //Toast.makeText(this, R.string.lbl_permission_notification, Toast.LENGTH_SHORT).show()
                binding.root.snacked(
                    message = R.string.lbl_permission_notification,
                    action = R.string.lbl_setting,
                    listener = {
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        intent.data = Uri.fromParts("package", packageName, null)
                        startActivity(intent)
                    }
                )
            }

            else -> {

                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 100)
            }

        }
    }

    companion object {
        const val CHANNEL_ID = "Kelas Industri"
        const val CHANNEL_NAME = "Kelas Industri Android"
        const val CHANNEL_DESCRIPTION = "Kelas Industri Android Level Middle"
    }
}