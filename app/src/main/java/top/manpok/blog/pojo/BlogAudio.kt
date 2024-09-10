package top.manpok.blog.pojo

data class BlogAudio(
    val data: List<Data?>?,
    override val currentPage: Int,
    override val noMore: Boolean,
    override val pageSize: Int,
    override val total: Int,
) : BasePaging() {
    data class Data(
        val album: String?,
        val artist: String?,
        val audioOrder: Int?,
        val audioUrl: String?,
        val coverUrl: String?,
        val createTime: String?,
        val id: String?,
        val name: String?,
        val updateTime: String?
    )
}