package com.example.foregroundservices

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder

class RunningService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    enum class Actions {
        START,
        STOP
    }

    private fun start() {
        val returnToActivityIntent = Intent(this, MainActivity::class.java)
        val resultPendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(returnToActivityIntent)
            getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

        val stopIntent = Intent(this, RunningService::class.java).apply {
            action = Actions.STOP.toString()
        }
        val stopPendingIntent = PendingIntent.getService(
            this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        val notification = NotificationCompat.Builder(
            this@RunningService,
            "running_channel").run {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle("Ongoing Foreground Service")
            setContentText("Click to return to app")
            setContentIntent(resultPendingIntent)
            addAction(R.drawable.ic_launcher_foreground, "Stop Foreground Service", stopPendingIntent)
            build()
        }
        startForeground(1, notification)
    }
}