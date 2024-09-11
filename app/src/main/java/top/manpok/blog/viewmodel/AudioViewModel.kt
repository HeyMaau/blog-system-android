package top.manpok.blog.viewmodel

import android.media.MediaPlayer
import android.text.TextUtils
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.manpok.blog.R
import top.manpok.blog.api.BlogRetrofit
import top.manpok.blog.pojo.BaseResponse
import top.manpok.blog.pojo.BlogAudio
import top.manpok.blog.utils.Constants
import top.manpok.blog.utils.LogUtil
import top.manpok.blog.utils.ToastUtil

class AudioViewModel : ViewModel() {

    private val TAG = "AudioViewModel"

    private val audioList = mutableListOf<BlogAudio.Data?>()

    private val mediaPlayer: MediaPlayer by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        MediaPlayer()
    }

    var currentIndex by mutableIntStateOf(0)
    var currentAudioName by mutableStateOf("")
    var currentAudioArtist by mutableStateOf("")
    var currentAudioUrl by mutableStateOf("")
    var currentAudioCover by mutableStateOf("")

    private val prepareStateMap: MutableMap<String, Boolean> = mutableMapOf()

    private var _playState = MutableStateFlow<PlayState>(PlayState.Stop)
    val playState: StateFlow<PlayState> = _playState.asStateFlow()

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
                                    currentAudioName = audioList[0]?.name ?: ""
                                    currentAudioArtist = audioList[0]?.artist ?: ""
                                    currentAudioUrl = audioList[0]?.audioUrl ?: ""
                                    currentAudioCover = audioList[0]?.coverUrl ?: ""
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<BaseResponse<BlogAudio>>, error: Throwable) {
                        LogUtil.e(TAG, "onFailure: $error")
                    }

                })
    }

    fun playOrPauseAudio() {
        if (TextUtils.isEmpty(currentAudioUrl)) {
            ToastUtil.showShortToast(R.string.play_audio_error)
            return
        }
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            _playState.value = PlayState.Pause
            return
        }
        if (prepareStateMap[currentAudioUrl] == true) {
            mediaPlayer.start()
            _playState.value = PlayState.Playing
            return
        }
        mediaPlayer.setDataSource(currentAudioUrl)
        mediaPlayer.prepareAsync()
        _playState.value = PlayState.PreParing
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
            _playState.value = PlayState.Playing
            prepareStateMap[currentAudioUrl] = true
        }
    }

    sealed class PlayState {
        data object Stop : PlayState()
        data object PreParing : PlayState()
        data object Playing : PlayState()
        data object Pause : PlayState()
        data object Error : PlayState()
    }
}