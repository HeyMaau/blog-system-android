package top.manpok.blog.base

import android.app.Application
import coil.Coil
import coil.ImageLoader
import okhttp3.Interceptor
import okhttp3.OkHttpClient

class BaseApplication : Application() {

    companion object {

        private lateinit var sApplication: Application
        fun getApplication(): Application = sApplication

        private lateinit var sDefaultUncaughtExceptionHandler: Thread.UncaughtExceptionHandler
        fun getDefaultUncaughtExceptionHandler(): Thread.UncaughtExceptionHandler =
            sDefaultUncaughtExceptionHandler
    }

    override fun onCreate() {
        super.onCreate()
        sApplication = this
        sDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()!!
        Thread.setDefaultUncaughtExceptionHandler(CustomExceptionHandler())
        registerActivityLifecycleCallbacks(BaseActivityLifecycleCallback())
        initCoilImageLoader()
    }

    private fun initCoilImageLoader() {
        val headerInterceptor = Interceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("referer", "https://m.manpok.top")
                .build()
            chain.proceed(newRequest)
        }

        // 创建 OkHttpClient 并添加拦截器
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .build()

        // 创建 ImageLoader 并设置 OkHttpClient
        val imageLoader = ImageLoader.Builder(this)
            .okHttpClient(okHttpClient)
            .build()

        // 将自定义的 ImageLoader 设置为全局默认
        Coil.setImageLoader(imageLoader)
    }
}