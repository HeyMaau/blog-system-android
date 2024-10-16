package top.manpok.blog.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaStyleNotificationHelper
import top.manpok.blog.R
import top.manpok.blog.base.BaseApplication
import top.manpok.blog.utils.Constants
import top.manpok.blog.viewmodel.GlobalViewModelManager

@UnstableApi
class AudioService : Service() {

    private val notificationBuilder by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        val application = BaseApplication.getApplication()
        val mediaSession = GlobalViewModelManager.audioViewModel.mediaSession
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_ID_AUDIO,
                application.getString(R.string.notification_channel_audio),
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
            NotificationCompat.Builder(
                BaseApplication.getApplication(),
                Constants.NOTIFICATION_CHANNEL_ID_AUDIO
            ).setContentTitle("Havana(feat.Young Thug")
                .setContentText("Camila Cabello - Havana")
                .setSmallIcon(R.drawable.ic_pause_floating)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOngoing(true)
                .setStyle(
                    MediaStyleNotificationHelper.MediaStyle(mediaSession)
//                        .setShowActionsInCompactView(0)
                )
        } else {
            NotificationCompat.Builder(
                BaseApplication.getApplication(),
            ).setContentTitle("Havana(feat.Young Thug")
                .setContentText("Camila Cabello - Havana")
                .setSmallIcon(R.drawable.ic_pause_floating)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOngoing(true)
                .setStyle(
                    MediaStyleNotificationHelper.MediaStyle(mediaSession)
//                        .setShowActionsInCompactView(0)
                )
        }
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                Constants.NOTIFICATION_ID_AUDIO,
                notificationBuilder.build(),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
            )
        } else {
            startForeground(Constants.NOTIFICATION_ID_AUDIO, notificationBuilder.build())
        }
    }
}