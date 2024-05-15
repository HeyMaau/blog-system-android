package top.manpok.blog.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import top.manpok.blog.db.entity.BlogArticleListItemForDB
import top.manpok.blog.utils.Constants

@Database(entities = [BlogArticleListItemForDB::class], version = 1, exportSchema = false)
abstract class ArticleListDatabase : RoomDatabase() {
    abstract fun articleListDao(): ArticleListDao

    companion object {
        @Volatile
        private var Instance: ArticleListDatabase? = null

        fun getDatabase(context: Context): ArticleListDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    ArticleListDatabase::class.java,
                    Constants.DB_NAME_ARTICLE
                )
                    .build()
                    .also { Instance = it }
            }
        }
    }
}