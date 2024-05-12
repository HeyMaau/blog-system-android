package top.manpok.blog.pojo

data class BaseResponse<T>(
    val code: Int?,
    val message: String?,
    val success: Boolean?,
    val data: T?
)