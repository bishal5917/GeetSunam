package com.example.geetsunam.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import com.example.geetsunam.R
import com.example.geetsunam.features.presentation.music.MusicPlayerActivity
import com.example.geetsunam.features.presentation.music.track.viewmodel.TrackSongEvent
import com.example.geetsunam.features.presentation.music.track.viewmodel.TrackSongViewModel
import com.example.geetsunam.features.presentation.music.viewmodel.MusicViewModel
import com.example.geetsunam.features.presentation.splash.viewmodel.SplashViewModel
import com.example.geetsunam.utils.LogUtil
import com.example.geetsunam.utils.models.CommonRequestModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SongTrackService : Service() {

    //Injecting
    @Inject
    lateinit var musicViewModel: MusicViewModel

    @Inject
    lateinit var splashViewModel: SplashViewModel

    @Inject
    lateinit var trackSongViewModel: TrackSongViewModel

    companion object {
        var isServiceRunning = false
    }

    private var mThread: Thread? = null
    private var mHandler: Handler? = null
    private var time = (30 * 1000).toLong()
    private var runnable: Runnable? = null

    override fun onBind(intent: Intent): IBinder? {
        // We don't provide binding, so return null
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        LogUtil.log("onStartCommand executed with startId: $startId")
        if (intent != null) {
            val action = intent.action
            LogUtil.log("Intent with action $action")
            when (action) {
                Actions.START.name -> startService()
                Actions.STOP.name -> stopService()
            }
        }
        // by returning this we make sure the service isnot restarted if the system kills the
        // service
        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        LogUtil.log("Service Created")
        val notification = createNotification()
        startForeground(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtil.log("Service Destroyed")
        stopService()
        mThread = null
        mHandler = null
    }

    private fun startService() {
        if (isServiceRunning) {
            isServiceRunning = false
            stopService()
        }
        LogUtil.log("Starting the foreground service...")
        isServiceRunning = true
        try {
            if (mHandler != null && runnable != null) {
                mHandler = null
                runnable = null
            }
            isServiceRunning = true
            mHandler = (Looper.myLooper())?.let { Handler(it) }
            LogUtil.log("Service Started")
            runnable = Runnable {
                try {
                    // Perform the API call here
                    hitAPI()
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    isServiceRunning = false
                    LogUtil.log("Service Finished")
                    stopSelf()
                }
            }
            mHandler!!.postDelayed(runnable!!, time)
        } catch (ex: Exception) {
            LogUtil.log("Exception = ${ex.message}")
        }
    }

    private fun stopService() {
        LogUtil.log("Stopping the foreground service")
        try {
            stopSelf()
        } catch (e: Exception) {
            LogUtil.log("Service stopped without being started: ${e.message}")
        }
        isServiceRunning = false
    }

    private fun hitAPI() {
        trackSongViewModel.onEvent(
            TrackSongEvent.TrackCurrentSong(
                CommonRequestModel(
                    token = splashViewModel.splashState.value?.userEntity?.token,
                    songId = musicViewModel.musicState.value?.currentSong?.id
                )
            )
        )
    }

    private fun createNotification(): Notification {
        val notificationChannelId = "TRACKING SERVICE CHANNEL"
        // depending on the Android API that we're dealing with we will have
        // to use a specific method to create the notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;
            val channel = NotificationChannel(
                notificationChannelId,
                "TRACKING SERVICE CHANNEL ID",
                NotificationManager.IMPORTANCE_HIGH
            ).let {
                it.description = "TRACKING SERVICE CHANNEL"
                it.enableLights(true)
                it.lightColor = Color.RED
                it.enableVibration(true)
                it.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                it
            }
            notificationManager.createNotificationChannel(channel)
        }

        val pendingIntent: PendingIntent =
            Intent(this, MusicPlayerActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
            }

        val builder: Notification.Builder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Notification.Builder(
                this, notificationChannelId
            ) else Notification.Builder(this)

        return builder.setContentTitle("Song Tracking Service").setContentText("Service Running")
            .setContentIntent(pendingIntent).setSmallIcon(R.mipmap.ic_launcher).setTicker("...")
            .setPriority(Notification.PRIORITY_HIGH) // for under android 26 compatibility
            .build()
    }
}

enum class Actions {
    START, STOP
}
