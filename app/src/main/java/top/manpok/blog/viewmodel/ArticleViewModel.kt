package top.manpok.blog.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.manpok.blog.api.BlogRetrofit
import top.manpok.blog.base.BaseApplication
import top.manpok.blog.db.ArticleListDatabase
import top.manpok.blog.db.entity.BlogArticleListItemForDB
import top.manpok.blog.pojo.BaseResponse
import top.manpok.blog.pojo.BlogArticle
import top.manpok.blog.utils.Constants

class ArticleViewModel : ViewModel() {

    private val TAG = "ArticleViewModel"

    val articleList = mutableStateListOf<BlogArticle.Data?>()
    var currentPage by mutableIntStateOf(1)
    var noMore by mutableStateOf(false)
    var pageSize by mutableIntStateOf(5)
    var total by mutableIntStateOf(0)

    @Volatile
    private var dbHasData: Boolean = false

    init {
        viewModelScope.launch {
            getDataFromDB()
        }
        getArticleList(currentPage, pageSize)
    }

    fun getArticleList(page: Int, size: Int) {
        BlogRetrofit.articleApi.getArticleList(page, size).enqueue(object :
            Callback<BaseResponse<BlogArticle>> {
            override fun onResponse(
                call: Call<BaseResponse<BlogArticle>>,
                response: Response<BaseResponse<BlogArticle>>
            ) {
                if (response.isSuccessful) {
                    if (response.body()?.code == Constants.CODE_SUCCESS) {
                        val blogArticle = response.body()?.data
                        currentPage = blogArticle?.currentPage!!
                        noMore = blogArticle.noMore
                        pageSize = blogArticle.pageSize
                        total = blogArticle.total
                        articleList.clear()
                        blogArticle.data?.let { articleList.addAll(it) }
                        viewModelScope.launch {
                            saveToDB(blogArticle.data)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<BlogArticle>>, error: Throwable) {
                Log.d(TAG, "onFailure: $error")
            }

        })
    }

    private suspend fun getDataFromDB() {
        val dataList =
            ArticleListDatabase.getDatabase(BaseApplication.getApplication()).articleListDao()
                .getAll()
        dbHasData = dataList.isNotEmpty()
        convertDBData(dataList)
    }

    private suspend fun saveToDB(data: List<BlogArticle.Data?>?) {
        val saveList = mutableListOf<BlogArticleListItemForDB>()
        data?.forEach {
            val item = BlogArticleListItemForDB(
                id = it?.id!!,
                title = it.title,
                avatar = it.user?.avatar,
                userName = it.user?.userName,
                it.content
            )
            saveList.add(item)
        }
        if (dbHasData) {
            ArticleListDatabase.getDatabase(BaseApplication.getApplication()).articleListDao()
                .updateAll(saveList.toList())
        } else {
            ArticleListDatabase.getDatabase(BaseApplication.getApplication()).articleListDao()
                .insertAll(saveList.toList())
        }
    }

    private fun convertDBData(data: List<BlogArticleListItemForDB>?) {
        data?.forEach {
            val item = BlogArticle.Data(
                id = it.id,
                title = it.title,
                user = BlogArticle.User(avatar = it.avatar, userName = it.userName),
                content = it.content
            )
            articleList.add(item)
        }
    }
}