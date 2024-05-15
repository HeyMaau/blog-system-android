package top.manpok.blog.pojo

data class BlogArticle(
    val data: List<Data?>?,
    override val currentPage: Int,
    override val noMore: Boolean,
    override val pageSize: Int,
    override val total: Int,
) : BasePaging() {
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
    ) {
        constructor(id: String, title: String?, user: User, content: String?) : this(
            category = null,
            categoryId = null,
            content = content,
            cover = null,
            createTime = null,
            id = id,
            labels = null,
            state = null,
            title = title,
            type = null,
            updateTime = null,
            user = user,
            userId = null,
            viewCount = null

        )
    }

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
    ) {
        constructor(avatar: String?, userName: String?) : this(
            avatar = avatar,
            createTime = null,
            email = null,
            hubSite = null,
            id = null,
            major = null,
            roles = null,
            sign = null,
            state = null,
            updateTime = null,
            userName = userName
        )
    }
}

