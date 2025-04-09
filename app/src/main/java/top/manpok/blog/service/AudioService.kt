package top.manpok.blog.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaStyleNotificationHelper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import top.manpok.blog.R
import top.manpok.blog.activity.AudioPlayerActivity
import top.manpok.blog.utils.Constants
import top.manpok.blog.viewmodel.AudioViewModel
import top.manpok.blog.viewmodel.GlobalViewModelManager.audioViewModel

@UnstableApi
class AudioService : Service() {

    private val ACTION_PLAY = "action_play"
    private val ACTION_PRE = "action_pre"
    private val ACTION_NEXT = "action_next"

    private var job: Job? = null
    private var playState: AudioViewModel.PlayState = AudioViewModel.PlayState.Stop

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        updateNotification()
        job = GlobalScope.launch {
            audioViewModel.playState.collect {
                playState = it
                updateNotification()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        updateNotification()
        when (intent?.action) {
            ACTION_PLAY -> {
                audioViewModel.playOrPauseAudio()
            }
            ACTION_PRE -> {
                audioViewModel.playPre()
            }
            ACTION_NEXT -> {
                audioViewModel.playNext()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_ID_AUDIO,
                getString(R.string.notification_channel_audio),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.description = getString(R.string.notification_channel_audio_description)
            val notificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun updateNotification() {
        val mediaSession = audioViewModel.mediaSession

        val contentIntent = Intent(this, AudioPlayerActivity::class.java)
        val pendingContentIntent = PendingIntent.getActivity(
            this, 4, contentIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val playIntent = Intent(this, AudioService::class.java).apply { action = ACTION_PLAY }
        val preIntent = Intent(this, AudioService::class.java).apply { action = ACTION_PRE }
        val nextIntent = Intent(this, AudioService::class.java).apply { action = ACTION_NEXT }

        val pendingPlayIntent = PendingIntent.getService(this, 1, playIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val pendingPreIntent = PendingIntent.getService(this, 2, preIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val pendingNextIntent = PendingIntent.getService(this, 3, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID_AUDIO)
            .setSmallIcon(R.mipmap.app_icon)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(pendingContentIntent)
            .setStyle(
                MediaStyleNotificationHelper.MediaStyle(mediaSession)
                    .setShowActionsInCompactView(0, 1, 2)
            )
            .setContentTitle(audioViewModel.currentAudioName)
            .setContentText(audioViewModel.currentAudioArtist)
            .setLargeIcon(audioViewModel.currentCoverBitmap)
            .addAction(R.drawable.ic_skip_previous, "Previous", pendingPreIntent)
            .addAction(
                if (playState is AudioViewModel.PlayState.Playing) R.drawable.ic_pause_notification else R.drawable.ic_play_notification,
                if (playState is AudioViewModel.PlayState.Playing) "Pause" else "Play",
                pendingPlayIntent
            )
            .addAction(R.drawable.ic_skip_next, "Next", pendingNextIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                Constants.NOTIFICATION_ID_AUDIO,
                builder.build(),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
            )
        } else {
            startForeground(Constants.NOTIFICATION_ID_AUDIO, builder.build())
        }
    }
}
