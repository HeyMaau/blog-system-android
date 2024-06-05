package top.manpok.blog.utils

object Constants {

    const val PACKAGE_NAME = "top.manpok.blog"

    const val CODE_SUCCESS: Int = 20000

    const val BASE_URL: String = "https://m.manpok.top"
    const val BASE_IMAGE_URL: String = BASE_URL + "/image/"

    const val TABLE_NAME_ARTICLE_LIST: String = "article_list"
    const val TABLE_NAME_THINKING_LIST: String = "thinking_list"
    const val TABLE_NAME_ARTICLE_DETAIL: String = "article_detail"
    const val TABLE_NAME_SEARCH_HISTORY: String = "search_history"

    const val DB_NAME_ARTICLE: String = "article_database"
    const val DB_NAME_THINKING: String = "thinking_database"
    const val DB_NAME_SEARCH: String = "search_database"

    const val DEFAULT_PAGE = 1
    const val DEFAULT_PAGE_SIZE = 10

    const val autoLoadMoreThreshold = 3
}