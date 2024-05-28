package top.manpok.blog.api

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import top.manpok.blog.utils.Constants
import java.util.concurrent.TimeUnit

object BlogRetrofit {

    private const val TAG = "BlogRetrofit"

    private val httpLoggingInterceptor: HttpLoggingInterceptor by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        val instance = HttpLoggingInterceptor {
            Log.i(TAG, it)
        }
        instance.setLevel(HttpLoggingInterceptor.Level.BODY)
        return@lazy instance
    }
    private val okHttpClient: OkHttpClient.Builder by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        val builder = OkHttpClient.Builder()
        builder.apply {
            addInterceptor(httpLoggingInterceptor)
            connectTimeout(60, TimeUnit.SECONDS)
            readTimeout(60, TimeUnit.SECONDS)
            writeTimeout(60, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
        }
        return@lazy builder
    }
    private val instance: Retrofit by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                okHttpClient.build()
            )
            .build()
    }

    val articleApi: ArticleApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        instance.create(ArticleApi::class.java)
    }

    val thinkingApi: ThinkingApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        instance.create(ThinkingApi::class.java)
    }

    val categoryApi: CategoryApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        instance.create(CategoryApi::class.java)
    }

    val userApi: UserApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        instance.create(UserApi::class.java)
    }

    val friendLinkApi: FriendLinkApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        instance.create(FriendLinkApi::class.java)
    }
}