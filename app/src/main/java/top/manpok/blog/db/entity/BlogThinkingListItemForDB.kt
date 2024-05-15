package top.manpok.blog.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import top.manpok.blog.utils.Constants

@Entity(tableName = Constants.TABLE_NAME_THINKING_LIST)
data class BlogThinkingListItemForDB(
    @PrimaryKey
    val id: String,
    val content: String?,
    val images: String?,
    val title: String?,
    val avatar: String?,
    val userName: String?
)