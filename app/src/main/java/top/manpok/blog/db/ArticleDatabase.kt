package top.manpok.blog.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import top.manpok.blog.db.entity.BlogArticleDetailForDB
import top.manpok.blog.db.entity.BlogArticleListItemForDB
import top.manpok.blog.utils.Constants

@Database(
    entities = [BlogArticleListItemForDB::class, BlogArticleDetailForDB::class],
    version = 2
)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun articleListDao(): ArticleListDao

    abstract fun articleDetailDao(): ArticleDetailDao

    companion object {

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE ${Constants.TABLE_NAME_ARTICLE_LIST} ADD `cover` TEXT")
            }
        }

        @Volatile
        private var Instance: ArticleDatabase? = null

        fun getDatabase(context: Context): ArticleDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    ArticleDatabase::class.java,
                    Constants.DB_NAME_ARTICLE
                ).addMigrations(MIGRATION_1_2)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}