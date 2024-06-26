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
        var content: String?,
        val cover: String?,
        val createTime: String?,
        val id: String?,
        val labels: String?,
        val searchItem: List<String?>?,
        var title: String?,
        val updateTime: String?,
        val viewCount: Int?,
        var titleList: List<String>?,
        var contentList: List<String>?,
    )
}

