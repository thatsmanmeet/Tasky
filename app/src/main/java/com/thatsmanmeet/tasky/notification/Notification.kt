package com.thatsmanmeet.tasky.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.thatsmanmeet.tasky.MainActivity
import com.thatsmanmeet.tasky.R

const val notificationID = 1
const val channelID = "Remainder Channel"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"

class Notification: BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent?) {

        val launchIntent = Intent(context, MainActivity::class.java)
        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity( context.applicationContext, 0, launchIntent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_check)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentText(intent.getStringExtra(messageExtra))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationID,notification)
    }
}