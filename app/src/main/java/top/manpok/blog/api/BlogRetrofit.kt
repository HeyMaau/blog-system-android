package top.manpok.blog.api

import android.os.Build
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import top.manpok.blog.base.BaseApplication
import top.manpok.blog.utils.Constants
import top.manpok.blog.utils.LogUtil
import top.manpok.blog.utils.TempData
import java.util.concurrent.TimeUnit

object BlogRetrofit {

    private const val TAG = "BlogRetrofit"

    private val httpLoggingInterceptor: HttpLoggingInterceptor by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        val instance = HttpLoggingInterceptor {
            LogUtil.i(TAG, it)
        }
        instance.setLevel(HttpLoggingInterceptor.Level.BODY)
        return@lazy instance
    }

    private val headerInterceptor: Interceptor by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        val packageManager = BaseApplication.getApplication().packageManager
        val versionName =
            packageManager.getPackageInfo(
                BaseApplication.getApplication().packageName,
                0
            ).versionName
        val interceptor = Interceptor { chain ->
            val build = chain.request().newBuilder()
                .addHeader(
                    "User-Agent",
                    "Android/${Build.VERSION.RELEASE} ${Build.BRAND}/${Build.MODEL} manpok_app/$versionName"
                )
                .addHeader("referer", "https://m.manpok.top")
                .build()
            return@Interceptor chain.proceed(build)
        }
        return@lazy interceptor
    }

    private val okHttpClient: OkHttpClient.Builder by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        val builder = OkHttpClient.Builder()
        builder.apply {
            addNetworkInterceptor(httpLoggingInterceptor)
            addInterceptor(headerInterceptor)
            connectTimeout(20, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
        }
        return@lazy builder
    }

    private val devInstance: Retrofit by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        Retrofit.Builder().baseUrl(Constants.BASE_URL_DEV)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                okHttpClient.build()
            )
            .build()
    }

    private val prodInstance: Retrofit by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                okHttpClient.build()
            )
            .build()
    }

    private val prodArticleApi: ArticleApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        prodInstance.create(ArticleApi::class.java)
    }

    private val devArticleApi: ArticleApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        devInstance.create(ArticleApi::class.java)
    }

    val articleApi: ArticleApi
        get() {
            return if (TempData.currentEnv == 0) {
                prodArticleApi
            } else {
                devArticleApi
            }
        }

    private val prodThinkingApi: ThinkingApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        prodInstance.create(ThinkingApi::class.java)
    }

    private val devThinkingApi: ThinkingApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        devInstance.create(ThinkingApi::class.java)
    }

    val thinkingApi: ThinkingApi
        get() {
            return if (TempData.currentEnv == 0) {
                prodThinkingApi
            } else {
                devThinkingApi
            }
        }

    private val prodCategoryApi: CategoryApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        prodInstance.create(CategoryApi::class.java)
    }

    private val devCategoryApi: CategoryApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        devInstance.create(CategoryApi::class.java)
    }

    val categoryApi: CategoryApi
        get() {
            return if (TempData.currentEnv == 0) {
                prodCategoryApi
            } else {
                devCategoryApi
            }
        }

    private val prodUserApi: UserApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        prodInstance.create(UserApi::class.java)
    }

    private val devUserApi: UserApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        devInstance.create(UserApi::class.java)
    }

    val userApi: UserApi
        get() {
            return if (TempData.currentEnv == 0) {
                prodUserApi
            } else {
                devUserApi
            }
        }

    private val prodFriendLinkApi: FriendLinkApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        prodInstance.create(FriendLinkApi::class.java)
    }

    private val devFriendLinkApi: FriendLinkApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        devInstance.create(FriendLinkApi::class.java)
    }

    val friendLinkApi: FriendLinkApi
        get() {
            return if (TempData.currentEnv == 0) {
                prodFriendLinkApi
            } else {
                devFriendLinkApi
            }
        }

    private val prodFeedbackApi: FeedbackApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        prodInstance.create(FeedbackApi::class.java)
    }

    private val devFeedbackApi: FeedbackApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        devInstance.create(FeedbackApi::class.java)
    }

    val feedbackApi: FeedbackApi
        get() {
            return if (TempData.currentEnv == 0) {
                prodFeedbackApi
            } else {
                devFeedbackApi
            }
        }

    private val prodSearchApi: SearchApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        prodInstance.create(SearchApi::class.java)
    }

    private val devSearchApi: SearchApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        devInstance.create(SearchApi::class.java)
    }

    val searchApi: SearchApi
        get() {
            return if (TempData.currentEnv == 0) {
                prodSearchApi
            } else {
                devSearchApi
            }
        }

    private val prodCommentApi: CommentApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        prodInstance.create(CommentApi::class.java)
    }

    private val devCommentApi: CommentApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        devInstance.create(CommentApi::class.java)
    }

    val commentApi: CommentApi
        get() {
            return if (TempData.currentEnv == 0) {
                prodCommentApi
            } else {
                devCommentApi
            }
        }

    private val prodUpdateApi: UpdateApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        prodInstance.create(UpdateApi::class.java)
    }

    private val devUpdateApi: UpdateApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        devInstance.create(UpdateApi::class.java)
    }

    val updateApi: UpdateApi
        get() {
            return if (TempData.currentEnv == 0) {
                prodUpdateApi
            } else {
                devUpdateApi
            }
        }

    private val prodAudioApi: AudioApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        prodInstance.create(AudioApi::class.java)
    }

    private val devAudioApi: AudioApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        devInstance.create(AudioApi::class.java)
    }

    val audioApi: AudioApi
        get() {
            return if (TempData.currentEnv == 0) {
                prodAudioApi
            } else {
                devAudioApi
            }
        }
}