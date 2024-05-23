package top.manpok.blog.pojo

data class BlogFriendLink(
    override val currentPage: Int,
    override val noMore: Boolean,
    override val pageSize: Int,
    override val total: Int,
    val data: List<Data?>?
) : BasePaging() {
    data class Data(
        val createTime: String,
        val id: String,
        val linkOrder: Int,
        val logo: Any,
        val name: String,
        val updateTime: String,
        val url: String,
    )
}