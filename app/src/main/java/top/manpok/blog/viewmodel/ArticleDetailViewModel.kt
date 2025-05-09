package top.manpok.blog.viewmodel

import android.content.Context
import android.content.res.Configuration
import android.text.TextUtils
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.manpok.blog.api.BlogRetrofit
import top.manpok.blog.base.BaseApplication
import top.manpok.blog.db.ArticleDatabase
import top.manpok.blog.db.entity.BlogArticleDetailForDB
import top.manpok.blog.pojo.BaseResponse
import top.manpok.blog.pojo.BlogArticleDetail
import top.manpok.blog.pojo.DefaultState
import top.manpok.blog.utils.Constants
import top.manpok.blog.utils.LogUtil
import top.manpok.blog.utils.NetworkUtil

class ArticleDetailViewModel : ViewModel() {

    private val TAG = "ArticleDetailViewModel"

    private val mutex = Mutex()

    var title by mutableStateOf("")
    var authorAvatar by mutableStateOf("")
    var authorName by mutableStateOf("")
    var authorSign by mutableStateOf("")
    var cover by mutableStateOf("")
    var content by mutableStateOf("")
    var updateTime by mutableStateOf("")
    var darkTheme by mutableStateOf(false)
    private var tempContent by mutableStateOf("")

    var loading by mutableStateOf(true)
    private var timeOut by mutableStateOf(false)

    val imageMap = mutableMapOf<String, Int>()
    val imageList = mutableListOf<String>()

    private var _articleDetailState = MutableStateFlow<DefaultState>(DefaultState.NONE)
    var articleDetailState = _articleDetailState.asStateFlow()

    init {
        checkAndSetDarkTheme(BaseApplication.getApplication())
        initLoading()
    }

    fun getArticleDetail(id: String?) {
        if (!NetworkUtil.isNetworkAvailable(BaseApplication.getApplication())) {
            if (TextUtils.isEmpty(content)) {
                _articleDetailState.value = DefaultState.NETWORK_ERROR
                loading = false
            }
            return
        }

        _articleDetailState.value = DefaultState.NONE

        if (id != null && !TextUtils.isEmpty(id)) {
            BlogRetrofit.articleApi.getArticleDetail(id)
                .enqueue(object : Callback<BaseResponse<BlogArticleDetail>> {
                    override fun onResponse(
                        call: Call<BaseResponse<BlogArticleDetail>>,
                        response: Response<BaseResponse<BlogArticleDetail>>
                    ) {
                        if (response.isSuccessful) {
                            val body = response.body()
                            if (body?.code == Constants.CODE_SUCCESS) {
                                val data = body.data
                                title = data?.title!!
                                authorAvatar =
                                    data.user?.avatar ?: ""
                                authorName = data.user?.userName!!
                                authorSign = data.user.sign!!
                                cover =
                                    data.cover ?: ""
                                updateTime = data.updateTime!!
                                setHtmlContent(data.content!!)
                                tempContent = data.content
                                if (timeOut) {
                                    loading = false
                                }
                                viewModelScope.launch(Dispatchers.IO) {
                                    mutex.withLock {
                                        saveToDB(data)
                                    }
                                    initImageMap(data.content)
                                }
                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<BaseResponse<BlogArticleDetail>>,
                        error: Throwable
                    ) {
                        if (timeOut) {
                            loading = false
                        }
                        viewModelScope.launch(Dispatchers.IO) {
                            LogUtil.e(TAG, "网络错误，从数据库中读取文章${id}的数据")
                            getFromDB(id)
                        }
                        LogUtil.e(TAG, "onFailure: $error")
                    }

                })
        }
    }

    suspend fun getFromDB(id: String?) {
        if (id == null || TextUtils.isEmpty(id)) {
            return
        }
        val articleDetailDao =
            ArticleDatabase.getDatabase(BaseApplication.getApplication()).articleDetailDao()
        val data = articleDetailDao.getOne(id)
        if (data != null) {
            withContext(Dispatchers.Main) {
                title = data.title!!
                authorAvatar =
                    data.avatar ?: ""
                authorName = data.userName!!
                authorSign = data.sign!!
                cover =
                    data.cover ?: ""
                setHtmlContent(data.content!!)
                tempContent = data.content
                updateTime = data.updateTime!!
            }
        } else {
            LogUtil.e(TAG, "文章${id}的数据库记录为空，展示错误页面")
            _articleDetailState.value = DefaultState.NETWORK_ERROR
        }
    }

    suspend fun saveToDB(data: BlogArticleDetail?) {
        val articleDetailDao =
            ArticleDatabase.getDatabase(BaseApplication.getApplication()).articleDetailDao()
        if (data != null) {
            val blogArticleDetailForDB = BlogArticleDetailForDB(
                content = data.content,
                cover = data.cover,
                id = data.id!!,
                title = data.title,
                updateTime = data.updateTime,
                avatar = data.user?.avatar,
                sign = data.user?.sign,
                userName = data.user?.userName
            )
            articleDetailDao.deleteOne(blogArticleDetailForDB)
            articleDetailDao.insert(blogArticleDetailForDB)
        }
    }

    private fun initImageMap(htmlContent: String) {
        val document = Jsoup.parse(htmlContent)
        val elements = document.select("img")
        elements.forEachIndexed { index, element ->
            val src = element.attr("src")
            imageMap[src] = index
            imageList.add(src)
        }
    }

    fun checkAndSetDarkTheme(context: Context) {
        val currentNightMode =
            context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_NO -> darkTheme =
                false // Night mode is not active, we're using the light theme.
            Configuration.UI_MODE_NIGHT_YES -> darkTheme =
                true // Night mode is active, we're using dark theme.
        }
    }

    fun resetContent() {
        setHtmlContent(tempContent)
    }

    private fun setHtmlContent(data: String) {
        val highlightCssLink =
            if (darkTheme) "<link rel=\"stylesheet\" href=\"highlight.dark.min.css\"/>" else "<link rel=\"stylesheet\" href=\"highlight.default.min.css\"/>"
        content = """
            <!DOCTYPE html>
            <html lang="zh">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width,initial-scale=1.0,user-scalable=no,maximum-scale=1.0,minimum-scale=1.0">
                <title>Article Title</title>
                $highlightCssLink
                <link rel="stylesheet" href="global.css"/>
                <link rel="stylesheet" href="article.detail.css"/>
                <script src="highlight.min.js"></script>
                <script src="groovy.min.js"></script>
            </head>
            <body>
                <div class="container">
                    $data
                </div>
            </body>
            <script>
                function blogHighlightAll() {
                    hljs.highlightAll()
                }
            </script>
            <script src="image-utils.js"></script>
            <script>
                function callAddImageOnClick() {
                    addImageOnClick()
                }
            </script>
            </html>
        """.trimIndent()
    }

    fun initLoading() {
        viewModelScope.launch {
            loading = true
            delay(1000)
            if (!TextUtils.isEmpty(content)) {
                loading = false
            }
            timeOut = true
        }
    }
}