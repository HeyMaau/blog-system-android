package top.manpok.blog.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import top.manpok.blog.db.entity.BlogArticleDetailForDB
import top.manpok.blog.db.entity.BlogArticleListItemForDB
import top.manpok.blog.utils.Constants

@Database(
    entities = [BlogArticleListItemForDB::class, BlogArticleDetailForDB::class],
    version = 1,
    exportSchema = false
)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun articleListDao(): ArticleListDao

    abstract fun articleDetailDao(): ArticleDetailDao

    companion object {
        @Volatile
        private var Instance: ArticleDatabase? = null

        fun getDatabase(context: Context): ArticleDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    ArticleDatabase::class.java,
                    Constants.DB_NAME_ARTICLE
                )
                    .build()
                    .also { Instance = it }
            }
        }
    }
}