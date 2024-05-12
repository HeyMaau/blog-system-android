package top.manpok.blog.api

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BlogRetrofit {

    private const val TAG = "BlogRetrofit"

    private const val baseUrl: String = "https://m.manpok.top"
    private val httpLoggingInterceptor: HttpLoggingInterceptor by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        val instance = HttpLoggingInterceptor {
            Log.i(TAG, it)
        }
        instance.setLevel(HttpLoggingInterceptor.Level.BODY)
        return@lazy instance
    }
    private val okHttpClient: OkHttpClient.Builder by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(httpLoggingInterceptor)
        return@lazy builder
    }
    private val instance: Retrofit by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
            .client(
                okHttpClient.build()
            )
            .build()
    }
    val articleApi: ArticleApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        instance.create(ArticleApi::class.java)
    }
}