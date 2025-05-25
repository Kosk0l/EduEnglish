package com.example.eduenglish.Notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.eduenglish.R
import com.example.eduenglish.ui.theme.SettingPreferences
import kotlinx.coroutines.flow.first

object NotificationHelper {

    suspend fun showTestNotification(context: Context) {
        val prefs = SettingPreferences(context)
        val notificationsEnabled = prefs.notificationEnabledFlow.first()
        if (!notificationsEnabled) return

        val channelId = "review_channel"
        val notificationId = 9999

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Тестовое уведомление"
            val descriptionText = "Канал для тестирования"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Тестовое уведомление")
            .setContentText("Это уведомление появилось вручную")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(notificationId, builder.build())
        }
    }
}