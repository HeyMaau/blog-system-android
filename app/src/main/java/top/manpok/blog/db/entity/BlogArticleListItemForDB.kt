package top.manpok.blog.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import top.manpok.blog.utils.Constants

@Entity(tableName = Constants.TABLE_NAME_ARTICLE_LIST)
data class BlogArticleListItemForDB(
    @PrimaryKey
    val id: Int,
    val title: String,
    val avatar: String,
    val userName: String,
    val content: String
)