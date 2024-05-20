package top.manpok.blog.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.manpok.blog.api.BlogRetrofit
import top.manpok.blog.pojo.BaseResponse
import top.manpok.blog.pojo.BlogCategory
import top.manpok.blog.utils.Constants

class CategoryViewModel : ViewModel() {
    private val TAG = "CategoryViewModel"

    val categoryList = mutableStateListOf<BlogCategory>()

    init {
        getCategoryList()
    }

    private fun getCategoryList() {
        BlogRetrofit.categoryApi.getCategoryList()
            .enqueue(object : Callback<BaseResponse<List<BlogCategory>>> {
                override fun onResponse(
                    call: Call<BaseResponse<List<BlogCategory>>>,
                    response: Response<BaseResponse<List<BlogCategory>>>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body?.code == Constants.CODE_SUCCESS) {
                            val data = body.data
                            if (data != null) {
                                categoryList.addAll(data)
                            }
                        }
                    }
                }

                override fun onFailure(
                    call: Call<BaseResponse<List<BlogCategory>>>,
                    error: Throwable
                ) {
                    Log.d(TAG, "onFailure: $error")
                }

            })
    }
}