package top.manpok.blog.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import top.manpok.blog.db.entity.BlogSearchHistory
import top.manpok.blog.utils.Constants

@Dao
interface SearchHistoryDao {

    @Insert
    suspend fun insert(blogSearchHistory: BlogSearchHistory)

    @Update
    suspend fun update(blogSearchHistory: BlogSearchHistory)

    @Delete
    suspend fun delete(blogSearchHistory: BlogSearchHistory)

    @Query("SELECT * FROM ${Constants.TABLE_NAME_SEARCH_HISTORY}")
    suspend fun getAll(): List<BlogSearchHistory>

    @Query("SELECT * FROM ${Constants.TABLE_NAME_SEARCH_HISTORY} WHERE keyword = :keyword")
    suspend fun getByKeyword(keyword: String): BlogSearchHistory?
}