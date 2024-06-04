package top.manpok.blog.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import top.manpok.blog.db.entity.BlogSearchHistory
import top.manpok.blog.utils.Constants

@Database(
    entities = [BlogSearchHistory::class],
    version = 1,
    exportSchema = false
)
abstract class SearchDatabase : RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao

    companion object {
        @Volatile
        private var Instance: SearchDatabase? = null

        fun getDatabase(context: Context): SearchDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    SearchDatabase::class.java,
                    Constants.DB_NAME_SEARCH
                )
                    .build()
                    .also { Instance = it }
            }
        }
    }
}