package top.manpok.blog.viewmodel

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
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

class AudioViewModel : ViewModel() {

    private val TAG = "AudioViewModel"

    val audioList = mutableListOf<BlogAudio.Data?>()

    private val exoPlayer: ExoPlayer by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        ExoPlayer.Builder(BaseApplication.getApplication()).build()
    }

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

    var currentTimerJob: Job? = null


    private var _playState = MutableStateFlow<PlayState>(PlayState.Stop)
    val playState: StateFlow<PlayState> = _playState.asStateFlow()

    private var isPrepare = false
    private var initFirstAudio = false

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
            return
        }
        if (!initFirstAudio) {
            _playState.value = PlayState.PreParing
        } else {
            _playState.value = PlayState.Playing
        }
        exoPlayer.play()
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