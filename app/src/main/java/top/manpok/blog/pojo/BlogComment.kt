package top.manpok.blog.pojo

data class BlogComment(
    override val currentPage: Int,
    override val noMore: Boolean,
    override val pageSize: Int,
    override val total: Int,
    val data: List<Data?>?
) : BasePaging() {
    data class Data(
        val id: String?,
        val articleId: String,
        val content: String,
        val parentCommentId: String?,
        val replyCommentId: String?,
        var replyUserName: String?,
        val type: Int,
        val userEmail: String,
        val userName: String,
        val userAvatar: String?,
        val updateTime: String?,
        val children: List<Data>?
    )
}