package com.example.decemberdef

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat

// Constants for notification
const val notificationID = "notificationID"
const val channelID = "HighImportance"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"

class Notification : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val genId = intent.getIntExtra(notificationID, 0)
        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(intent.getStringExtra(titleExtra)) // Set title from intent
            .setContentText(intent.getStringExtra(messageExtra)) // Set content text from intent
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        manager.notify(genId, notification)
        Log.d(TAG, "NotificationId: $genId")
    }
}