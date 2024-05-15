package top.manpok.blog.pojo

abstract class BasePaging {
    abstract val currentPage: Int
    abstract val noMore: Boolean
    abstract val pageSize: Int
    abstract val total: Int
}
