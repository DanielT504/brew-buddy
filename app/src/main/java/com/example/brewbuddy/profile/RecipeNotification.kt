package com.example.brewbuddy.profile

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.brewbuddy.MainActivity
import com.example.brewbuddy.R

fun showNotificationForNewRecipes(context: Context, newItem: List<String>) {
    // Step 1: Create a Notification Channel (for Android 8.0 Oreo and above)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channelId = "item_channel_id"
        val channelName = "New Store Item Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance)

        // Register the channel with the system
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager?.createNotificationChannel(channel)
    }

    // Step 2: Build the Notification
    val channelId = "item_channel_id"
    val notificationBuilder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.icon_drink) // Set your notification icon here
        .setContentTitle("New Items Added!")
        .setContentText("There are new drinks available at your favourite store!")
        .setAutoCancel(true) // The notification will be dismissed when tapped

    // Add an intent to launch your app's main activity when the notification is tapped
    val intent = Intent(context, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    notificationBuilder.setContentIntent(pendingIntent)

    // Step 3: Show the Notification
    val notificationId = 123 // A unique ID for the notification
    val notificationManager = NotificationManagerCompat.from(context)
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // If the permission is not granted, request it
        ActivityCompat.requestPermissions(
            context as Activity, // Ensure your Context is an instance of Activity
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            PERMISSION_REQUEST_CODE
        )
        return
    }
    notificationManager.notify(notificationId, notificationBuilder.build())
}

// Define a constant for the permission request code
private const val PERMISSION_REQUEST_CODE = 100
