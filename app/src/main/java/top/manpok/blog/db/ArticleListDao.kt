package top.manpok.blog.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import top.manpok.blog.db.entity.BlogArticleListItemForDB
import top.manpok.blog.utils.Constants

@Dao
interface ArticleListDao {

    @Insert
    suspend fun insertAll(articleList: List<BlogArticleListItemForDB>)

    @Update
    suspend fun updateAll(articleList: List<BlogArticleListItemForDB>): Int

    @Query("DELETE FROM ${Constants.TABLE_NAME_ARTICLE_LIST}")
    suspend fun deleteAll(): Int

    @Query("SELECT * FROM ${Constants.TABLE_NAME_ARTICLE_LIST}")
    suspend fun getAll(): List<BlogArticleListItemForDB>
}