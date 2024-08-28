package top.manpok.blog.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import top.manpok.blog.db.entity.BlogThinkingListItemForDB
import top.manpok.blog.utils.Constants

@Dao
interface ThinkingListDao {

    @Insert
    @Transaction
    suspend fun insertAll(thinkingList: List<BlogThinkingListItemForDB>)

    @Update
    @Transaction
    suspend fun updateAll(thinkingList: List<BlogThinkingListItemForDB>): Int

    @Query("DELETE FROM ${Constants.TABLE_NAME_THINKING_LIST}")
    suspend fun deleteAll(): Int

    @Query("SELECT * FROM ${Constants.TABLE_NAME_THINKING_LIST}")
    suspend fun getAll(): List<BlogThinkingListItemForDB>
}