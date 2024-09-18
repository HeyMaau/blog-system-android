package top.manpok.blog.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val audioList = mutableListOf<BlogAudio.Data?>()

    private val exoPlayer: ExoPlayer by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        ExoPlayer.Builder(BaseApplication.getApplication()).build()
    }

    var currentIndex by mutableIntStateOf(0)
    var currentAudioName by mutableStateOf("")
    var currentAudioArtist by mutableStateOf("")
    var currentAudioUrl by mutableStateOf("")
    var currentAudioCover by mutableStateOf("")


    private var _playState = MutableStateFlow<PlayState>(PlayState.Stop)
    val playState: StateFlow<PlayState> = _playState.asStateFlow()

    private var isPrepare = false

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
                }
            }
        })
        exoPlayer.playWhenReady = false
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

    private fun handlePlay() {
        if (exoPlayer.isLoading) {
            _playState.value = PlayState.PreParing
        } else {
            exoPlayer.play()
            _playState.value = PlayState.Playing
        }
    }

    fun playOrPauseAudio() {
        checkPrepare()
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
            _playState.value = PlayState.Pause
            return
        }
        handlePlay()
    }

    fun playNext() {
        if (currentIndex == audioList.size - 1) {
            ToastUtil.showShortToast(R.string.no_next_audio)
            return
        }
        currentIndex++
        setCurrentData(currentIndex)
        exoPlayer.seekToNextMediaItem()
        checkPrepare()
        handlePlay()
    }

    fun playPre() {
        if (currentIndex == 0) {
            ToastUtil.showShortToast(R.string.no_pre_audio)
            return
        }
        currentIndex--
        setCurrentData(currentIndex)
        exoPlayer.seekToPreviousMediaItem()
        checkPrepare()
        handlePlay()
    }

    fun onDestroy() {
        exoPlayer.release()
    }

    sealed class PlayState {
        data object Stop : PlayState()
        data object PreParing : PlayState()
        data object Playing : PlayState()
        data object Pause : PlayState()
        data object Error : PlayState()
    }
}