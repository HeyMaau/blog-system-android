package top.manpok.blog.pojo

data class BlogFriendLink(
    override val currentPage: Int,
    override val noMore: Boolean,
    override val pageSize: Int,
    override val total: Int,
    val data: List<Data?>?
) : BasePaging() {
    data class Data(
        val id: String,
        val createTime: String?,
        val linkOrder: Int?,
        val logo: String?,
        val name: String?,
        val updateTime: String?,
        val url: String?,
    )
}