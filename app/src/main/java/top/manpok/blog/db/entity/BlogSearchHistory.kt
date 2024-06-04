package top.manpok.blog.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import top.manpok.blog.utils.Constants

@Entity(tableName = Constants.TABLE_NAME_SEARCH_HISTORY)
data class BlogSearchHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val keyword: String,
    var count: Long,
    var updateTime: Long
)
