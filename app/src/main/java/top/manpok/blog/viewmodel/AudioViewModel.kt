package top.manpok.blog.viewmodel

import android.media.MediaPlayer
import android.text.TextUtils
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
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

    var currentAudioName by mutableStateOf("")
    var currentAudioArtist by mutableStateOf("")
    var currentAudioUrl by mutableStateOf("")
    var currentAudioCover by mutableStateOf("")

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

    fun playAudio() {
        if (TextUtils.isEmpty(currentAudioUrl)) {
            ToastUtil.showShortToast(R.string.play_audio_error)
            return
        }
        mediaPlayer.setDataSource(currentAudioUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            LogUtil.d(TAG, "onPrepared!")
            mediaPlayer.start()
        }
    }
}