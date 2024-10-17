package top.manpok.blog.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.session.MediaSession
import androidx.palette.graphics.Palette
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.manpok.blog.R
import top.manpok.blog.api.BlogRetrofit
import top.manpok.blog.base.BaseApplication
import top.manpok.blog.pojo.BaseResponse
import top.manpok.blog.pojo.BlogAudio
import top.manpok.blog.utils.Constants
import top.manpok.blog.utils.LogUtil
import top.manpok.blog.utils.ToastUtil
import java.io.File

@UnstableApi
class AudioViewModel : ViewModel() {

    private val TAG = "AudioViewModel"

    val mediaSession by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        MediaSession.Builder(BaseApplication.getApplication(), exoPlayer).build()
    }

    private val databaseProvider by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        StandaloneDatabaseProvider(BaseApplication.getApplication())
    }

    private val exoPlayer: ExoPlayer by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        val downloadDirectory =
            File(BaseApplication.getApplication().externalCacheDir, "AudioCache")
        val cache =
            SimpleCache(
                downloadDirectory,
                LeastRecentlyUsedCacheEvictor(Constants.MAX_AUDIO_CACHE_SIZE),
                databaseProvider
            )
        val cacheDataSourceFactory =
            CacheDataSource.Factory()
                .setCache(cache)
                .setUpstreamDataSourceFactory(DefaultHttpDataSource.Factory())
        ExoPlayer.Builder(BaseApplication.getApplication()).setMediaSourceFactory(
            DefaultMediaSourceFactory(BaseApplication.getApplication()).setDataSourceFactory(
                cacheDataSourceFactory
            )
        ).build()
    }

    val audioList = mutableListOf<BlogAudio.Data?>()
    var currentIndex by mutableIntStateOf(0)
    var currentAudioName by mutableStateOf("")
    var currentAudioArtist by mutableStateOf("")
    var currentAudioUrl by mutableStateOf("")
    var currentAudioCover by mutableStateOf("")
    var currentAudioDuration by mutableLongStateOf(0)
    var currentAudioDurationStr by mutableStateOf("00:00")
    var currentAudioPosition by mutableLongStateOf(0)
    var currentAudioPositionStr by mutableStateOf("00:00")
    var currentPlayMode by mutableIntStateOf(Constants.PLAY_MODE_SEQUENTIAL_PLAYBACK)
    var currentCoverBitmap: Bitmap? = null
    var currentBackgroundColor by mutableIntStateOf(
        BaseApplication.getApplication().getColor(R.color.purple_5068a0)
    )

    private var currentTimerJob: Job? = null


    private var _playState = MutableStateFlow<PlayState>(PlayState.Stop)
    val playState: StateFlow<PlayState> = _playState.asStateFlow()

    private var isPrepare = false
    private var initFirstAudio = false
    private var isEnd = false

    private var _refreshUIEvent = MutableStateFlow<Any?>(null)
    val refreshUIEvent: StateFlow<Any?> = _refreshUIEvent.asStateFlow()

    init {
        getAudioList()
    }

    private fun getAudioList() {
        BlogRetrofit.audioApi.getAudioList(Constants.DEFAULT_PAGE, Constants.DEFAULT_PAGE_SIZE)
            .enqueue(
                object : Callback<BaseResponse<BlogAudio>> {
                    override fun onResponse(
                        call: Call<BaseResponse<BlogAudio>>,
                        response: Response<BaseResponse<BlogAudio>>
                    ) {
                        if (response.isSuccessful) {
                            val body = response.body()
                            if (body?.code == Constants.CODE_SUCCESS) {
                                val blogAudio = body.data
                                blogAudio?.data?.let {
                                    audioList.addAll(it)
                                    initExoPlayer(it)
                                    setCurrentData(0)
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<BaseResponse<BlogAudio>>, error: Throwable) {
                        LogUtil.e(TAG, "onFailure: $error")
                    }

                })
    }

    private fun initExoPlayer(dataList: List<BlogAudio.Data?>) {
        val audioItemList: MutableList<MediaItem> = mutableListOf()
        dataList.forEach {
            if (it?.audioUrl != null) {
                audioItemList.add(MediaItem.fromUri(it.audioUrl))
            }
        }
        exoPlayer.setMediaItems(audioItemList)
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    exoPlayer.play()
                    _playState.value = PlayState.Playing
                    if (!initFirstAudio) {
                        initFirstAudio = true
                    }
                    setAudioDuration(exoPlayer.duration)
                }
                if (playbackState == Player.STATE_ENDED) {
                    _playState.value = PlayState.Stop
                    isEnd = true
                }
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                currentIndex = exoPlayer.currentMediaItemIndex
                setCurrentData(currentIndex)
                setAudioDuration(exoPlayer.duration)
            }
        })
        exoPlayer.playWhenReady = false
        setPositionTimer()
    }

    private fun setPositionTimer() {
        currentTimerJob = viewModelScope.launch {
            while (true) {
                currentAudioPosition = exoPlayer.currentPosition / 1000
                val min: Long = currentAudioPosition / 60
                val second: Long = currentAudioPosition % 60
                currentAudioPositionStr = String.format("%02d:%02d", min, second)
                delay(1000)
            }
        }
    }

    private fun setAudioDuration(duration: Long) {
        if (duration == C.TIME_UNSET) {
            return
        }
        currentAudioDuration = duration / 1000
        val min: Long = duration / 1000 / 60
        val second: Long = duration / 1000 % 60
        currentAudioDurationStr = String.format("%02d:%02d", min, second)
    }

    private fun setCurrentData(index: Int) {
        currentAudioName = audioList[index]?.name ?: ""
        currentAudioArtist = audioList[index]?.artist ?: ""
        currentAudioUrl = audioList[index]?.audioUrl ?: ""
        currentAudioCover = audioList[index]?.coverUrl ?: ""
    }

    private fun checkPrepare() {
        if (!isPrepare) {
            exoPlayer.prepare()
            _playState.value = PlayState.PreParing
            isPrepare = true
        }
    }

    fun playOrPauseAudio() {
        checkPrepare()
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
            _playState.value = PlayState.Pause
            sendRefreshUIEvent()
            return
        }
        if (!initFirstAudio) {
            _playState.value = PlayState.PreParing
        } else {
            _playState.value = PlayState.Playing
        }
        if (!isEnd) {
            exoPlayer.play()
        } else {
            exoPlayer.seekTo(exoPlayer.currentMediaItemIndex, 0)
            isEnd = false
        }
        sendRefreshUIEvent()
    }

    fun playNext() {
        if (!exoPlayer.hasNextMediaItem() && !exoPlayer.shuffleModeEnabled) {
            ToastUtil.showShortToast(R.string.no_next_audio)
            return
        }
        _playState.value = PlayState.PreParing
        exoPlayer.seekToNextMediaItem()
        currentIndex = exoPlayer.currentMediaItemIndex
        setCurrentData(currentIndex)
        checkPrepare()
        sendRefreshUIEvent()
    }

    fun playPre() {
        if (!exoPlayer.hasPreviousMediaItem() && !exoPlayer.shuffleModeEnabled) {
            ToastUtil.showShortToast(R.string.no_pre_audio)
            return
        }
        _playState.value = PlayState.PreParing
        exoPlayer.seekToPreviousMediaItem()
        currentIndex = exoPlayer.currentMediaItemIndex
        setCurrentData(currentIndex)
        checkPrepare()
        sendRefreshUIEvent()
    }

    fun seekTo(position: Long) {
        exoPlayer.seekTo(position * 1000)
    }

    fun changePlayMode(mode: Int) {
        when (mode) {
            Constants.PLAY_MODE_SEQUENTIAL_PLAYBACK -> {
                exoPlayer.shuffleModeEnabled = false
                exoPlayer.repeatMode = Player.REPEAT_MODE_ALL
                ToastUtil.showShortToast(R.string.list_loop)
            }

            Constants.PLAY_MODE_REPEAT_MODE_ONE -> {
                exoPlayer.shuffleModeEnabled = false
                exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
                ToastUtil.showShortToast(R.string.repeat_one)
            }

            Constants.PLAY_MODE_SHUFFLE_PLAYBACK -> {
                exoPlayer.shuffleModeEnabled = true
                exoPlayer.repeatMode = Player.REPEAT_MODE_ALL
                ToastUtil.showShortToast(R.string.shuffle_mode)
            }
        }
        currentPlayMode = mode
    }

    fun seekToAudio(index: Int) {
        _playState.value = PlayState.PreParing
        exoPlayer.seekTo(index, 0)
        currentIndex = exoPlayer.currentMediaItemIndex
        setCurrentData(currentIndex)
        checkPrepare()
        sendRefreshUIEvent()
    }

    private fun sendRefreshUIEvent() {
        viewModelScope.launch {
            _refreshUIEvent.emit(Any())
        }
    }

    fun getCoverBitmapAndPalette(url: String?) {
        if (TextUtils.isEmpty(url) || url == null) {
            return;
        }
        BlogRetrofit.imageApi.getImageStream(url).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    currentCoverBitmap = BitmapFactory.decodeStream(response.body()?.byteStream())
                    if (currentCoverBitmap != null) {
                        val context = BaseApplication.getApplication().applicationContext
                        Palette.from(currentCoverBitmap!!).generate {
                            var defaultColor: Int
                            defaultColor =
                                it?.getMutedColor(context.getColor(R.color.purple_5068a0))!!
                            if (defaultColor == context.getColor(R.color.purple_5068a0)) {
                                defaultColor =
                                    it.getDarkVibrantColor(context.getColor(R.color.purple_5068a0))
                            }
                            currentBackgroundColor = defaultColor
                        }
                    }
                    sendRefreshUIEvent()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, error: Throwable) {
                LogUtil.e(TAG, "getCoverBitmapAndPalette error: $error")
            }

        })
    }

    fun onDestroy() {
        exoPlayer.release()
        currentTimerJob?.cancel()
    }

    sealed class PlayState {
        data object Stop : PlayState()
        data object PreParing : PlayState()
        data object Playing : PlayState()
        data object Pause : PlayState()
        data object Error : PlayState()
    }
}