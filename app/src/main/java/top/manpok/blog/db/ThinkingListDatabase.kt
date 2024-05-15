package top.manpok.blog.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import top.manpok.blog.db.entity.BlogThinkingListItemForDB
import top.manpok.blog.utils.Constants

@Database(entities = [BlogThinkingListItemForDB::class], version = 1, exportSchema = false)
abstract class ThinkingListDatabase : RoomDatabase() {
    abstract fun thinkingListDao(): ThinkingListDao

    companion object {
        @Volatile
        private var Instance: ThinkingListDatabase? = null

        fun getDatabase(context: Context): ThinkingListDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    ThinkingListDatabase::class.java,
                    Constants.DB_NAME_THINKING
                )
                    .build()
                    .also { Instance = it }
            }
        }
    }
}