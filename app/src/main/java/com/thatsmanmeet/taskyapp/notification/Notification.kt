package com.thatsmanmeet.taskyapp.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.thatsmanmeet.taskyapp.MainActivity
import com.thatsmanmeet.taskyapp.R
import kotlin.random.Random

const val notificationID = 1
const val channelID = "Reminder Channel"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"

//  can be used in notification below to show additional text in notification
// .setContentText(intent.getStringExtra(messageExtra))

class Notification: BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent?) {

        val launchIntent = Intent(context, MainActivity::class.java)
        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity( context.applicationContext, 0, launchIntent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_check)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(Random.nextInt(0,1000) - Random.nextInt(25,75),notification)
    }
}