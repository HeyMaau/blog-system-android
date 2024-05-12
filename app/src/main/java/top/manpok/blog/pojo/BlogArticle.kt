package top.manpok.blog.pojo

data class BlogArticle(
    val currentPage: Int?,
    val data: List<Data?>?,
    val noMore: Boolean?,
    val pageSize: Int?,
    val total: Int?
) {
    data class Data(
        val category: Category?,
        val categoryId: String?,
        val content: String?,
        val cover: String?,
        val createTime: String?,
        val id: String?,
        val labels: String?,
        val state: String?,
        val title: String?,
        val type: String?,
        val updateTime: String?,
        val user: User?,
        val userId: String?,
        val viewCount: Int?
    )

    data class Category(
        val cover: String?,
        val createTime: String?,
        val description: String?,
        val id: String?,
        val name: String?,
        val state: String?,
        val tagColor: String?,
        val updateTime: String?
    )

    data class User(
        val avatar: String?,
        val createTime: String?,
        val email: String?,
        val hubSite: String?,
        val id: String?,
        val major: String?,
        val roles: String?,
        val sign: String?,
        val state: String?,
        val updateTime: String?,
        val userName: String?
    )
}

