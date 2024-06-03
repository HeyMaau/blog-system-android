package top.manpok.blog.pojo

data class BlogSearchResult(
    val data: List<Data?>?,
    override val currentPage: Int,
    override val noMore: Boolean,
    override val pageSize: Int,
    override val total: Int,
) : BasePaging() {
    data class Data(
        val categoryID: String?,
        val content: String?,
        val cover: String?,
        val createTime: String?,
        val id: String?,
        val labels: String?,
        val searchItem: List<String?>?,
        val title: String?,
        val updateTime: String?,
        val viewCount: Int?
    )
}

