package top.manpok.blog.pojo

data class BlogThinking(
    val data: List<Data>?,
    override val currentPage: Int,
    override val noMore: Boolean,
    override val pageSize: Int,
    override val total: Int,
) : BasePaging() {
    data class Data(
        val content: String?,
        val createTime: String?,
        val id: String?,
        val images: String?,
        val state: String?,
        val title: String?,
        val updateTime: String?,
        val user: User?,
        val userId: String?
    ) {
        constructor(
            id: String?,
            title: String?,
            user: User?,
            content: String?,
            images: String?,
            updateTime: String?
        ) : this(
            content = content,
            createTime = null,
            id = id,
            images = images,
            state = null,
            title = title,
            user = user,
            updateTime = updateTime,
            userId = null
        )
    }

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
        constructor(avatar: String?, userName: String?, sign: String?) : this(
            avatar = avatar,
            createTime = null,
            email = null,
            hubSite = null,
            id = null,
            major = null,
            roles = null,
            sign = sign,
            state = null,
            updateTime = null,
            userName = userName
        )
    }
}

