package top.manpok.blog.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
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

    private val ACTION_PLAY = "action_play"
    private val ACTION_PRE = "action_pre"
    private val ACTION_NEXT = "action_next"

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

            val playIntent = Intent(this, AudioService::class.java)
            playIntent.action = ACTION_PLAY
            val pendingPlayIntent = PendingIntent.getService(
                this, 1, playIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val preIntent = Intent(this, AudioService::class.java)
            preIntent.action = ACTION_PRE
            val pendingPreIntent = PendingIntent.getService(
                this, 2, preIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val nextIntent = Intent(this, AudioService::class.java)
            nextIntent.action = ACTION_NEXT
            val pendingNextIntent = PendingIntent.getService(
                this, 3, nextIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            NotificationCompat.Builder(
                BaseApplication.getApplication(),
                Constants.NOTIFICATION_CHANNEL_ID_AUDIO
            ).setContentTitle("Havana(feat.Young Thug")
                .setContentText("Camila Cabello - Havana")
                .setSmallIcon(R.drawable.ic_pause_floating)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOngoing(true)
                .addAction(R.drawable.ic_skip_previous, "Play previous", pendingPreIntent)
                .addAction(R.drawable.ic_play_floating, "Play", pendingPlayIntent)
                .addAction(R.drawable.ic_skip_next, "Play next", pendingNextIntent)
                .setStyle(
                    MediaStyleNotificationHelper.MediaStyle(mediaSession)
                        .setShowActionsInCompactView(0, 1, 2)
                )
        } else {
            val playIntent = Intent(this, AudioService::class.java)
            playIntent.action = ACTION_PLAY
            val pendingPlayIntent = PendingIntent.getService(
                this, 1, playIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val preIntent = Intent(this, AudioService::class.java)
            preIntent.action = ACTION_PRE
            val pendingPreIntent = PendingIntent.getService(
                this, 2, preIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val nextIntent = Intent(this, AudioService::class.java)
            nextIntent.action = ACTION_NEXT
            val pendingNextIntent = PendingIntent.getService(
                this, 3, nextIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            NotificationCompat.Builder(
                BaseApplication.getApplication(),
            ).setContentTitle("Havana(feat.Young Thug")
                .setContentText("Camila Cabello - Havana")
                .setSmallIcon(R.drawable.ic_pause_floating)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOngoing(true)
                .addAction(R.drawable.ic_skip_previous, "Play previous", pendingPreIntent)
                .addAction(R.drawable.ic_play_floating, "Play", pendingPlayIntent)
                .addAction(R.drawable.ic_skip_next, "Play next", pendingNextIntent)
                .setStyle(
                    MediaStyleNotificationHelper.MediaStyle(mediaSession)
                        .setShowActionsInCompactView(0, 1, 2)
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