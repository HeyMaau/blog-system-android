package top.manpok.blog.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import top.manpok.blog.utils.Constants

@Entity(tableName = Constants.TABLE_NAME_ARTICLE_DETAIL)
data class BlogArticleDetailForDB(
    @PrimaryKey
    val id: String,
    val content: String?,
    val cover: String?,
    val title: String?,
    val updateTime: String?,
    val avatar: String?,
    val sign: String?,
    val userName: String?
)
