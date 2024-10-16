package com.example.foregroundservices

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat

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
        val notification = NotificationCompat.Builder(
            this@RunningService,
            "running_channel").run {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle("Running")
            setContentText("Elapsed Time: 00:00:00")
            build()
        }
        startForeground(1, notification)
    }
}