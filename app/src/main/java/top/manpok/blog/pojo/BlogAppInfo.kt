package top.manpok.blog.pojo

data class BlogAppInfo(
    val appName: String?,
    val changeLog: String?,
    val createTime: String?,
    val downloadUrl: String?,
    val id: String,
    val updateTime: String?,
    val versionCode: Int,
    val versionName: String?,
    val forceUpdate: Int
)