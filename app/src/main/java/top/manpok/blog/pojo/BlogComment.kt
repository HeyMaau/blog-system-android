package top.manpok.blog.pojo

data class BlogComment(
    override val currentPage: Int,
    override val noMore: Boolean,
    override val pageSize: Int,
    override val total: Int,
    val data: List<Data?>?
) : BasePaging() {
    data class Data(
        val articleId: String,
        val content: String,
        val parentCommentId: String?,
        val replyCommentId: String?,
        val replyUserName: String?,
        val type: String,
        val userEmail: String,
        val userName: String
    )
}