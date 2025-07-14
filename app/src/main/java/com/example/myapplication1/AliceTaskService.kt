package com.example.myapplication1

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * This is a placeholder for our Foreground Service.
 * It will handle long-running AI tasks in the background in a future update.
 * For now, its methods are empty, but the file needs to exist.
 */
class AliceTaskService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        // We don't provide binding, so return null
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // This is where we would start our background task and show a notification.
        // We will implement this logic later.
        
        // Stop the service immediately since it does nothing for now.
        stopSelf()

        return START_NOT_STICKY
    }
}