package top.manpok.blog.utils

object Constants {

    const val PACKAGE_NAME = "top.manpok.blog"

    const val CODE_SUCCESS: Int = 20000

    const val BASE_URL: String = "https://m.manpok.top"
    const val BASE_IMAGE_URL: String = "$BASE_URL/image/"
    const val BASE_COMMENT_AVATAR_URL: String = BASE_IMAGE_URL + "comment/"

    const val BASE_URL_DEV: String = "http://192.168.137.129:8080"
    const val BASE_IMAGE_URL_DEV: String = "$BASE_URL_DEV/image/"
    const val BASE_COMMENT_AVATAR_URL_DEV: String = BASE_IMAGE_URL_DEV + "comment/"

    const val ENV_PROD = 0
    const val ENV_DEV = 1

    const val HOST_NAME = "manpok.top"

    const val TABLE_NAME_ARTICLE_LIST: String = "article_list"
    const val TABLE_NAME_THINKING_LIST: String = "thinking_list"
    const val TABLE_NAME_ARTICLE_DETAIL: String = "article_detail"
    const val TABLE_NAME_SEARCH_HISTORY: String = "search_history"

    const val DB_NAME_ARTICLE: String = "article_database"
    const val DB_NAME_THINKING: String = "thinking_database"
    const val DB_NAME_SEARCH: String = "search_database"

    const val DEFAULT_PAGE = 1
    const val DEFAULT_PAGE_SIZE = 10

    const val AUTO_LOAD_MORE_THRESHOLD = 3

    const val COMMENT_TYPE_ARTICLE = 0
    const val COMMENT_TYPE_THINKING = 1

    const val EMOJI_NUM_PER_PAGE = 24
}