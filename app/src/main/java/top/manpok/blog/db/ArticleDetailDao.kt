package top.manpok.blog.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import top.manpok.blog.db.entity.BlogArticleDetailForDB
import top.manpok.blog.utils.Constants

@Dao
interface ArticleDetailDao {

    @Insert
    @Transaction
    suspend fun insert(articleDetailForDB: BlogArticleDetailForDB)

    @Update
    @Transaction
    suspend fun update(articleDetailForDB: BlogArticleDetailForDB): Int

    @Delete
    suspend fun deleteOne(articleDetailForDB: BlogArticleDetailForDB): Int

    @Query("DELETE FROM ${Constants.TABLE_NAME_ARTICLE_DETAIL}")
    suspend fun deleteAll(): Int

    @Query("SELECT * FROM ${Constants.TABLE_NAME_ARTICLE_DETAIL} WHERE id = :id")
    suspend fun getOne(id: String): BlogArticleDetailForDB?
}