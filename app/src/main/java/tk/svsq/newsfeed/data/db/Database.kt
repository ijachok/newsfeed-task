package tk.svsq.newsfeed.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import tk.svsq.newsfeed.DATABASE_VERSION
import tk.svsq.newsfeed.data.db.dao.NewsDao
import tk.svsq.newsfeed.data.db.entities.ArticleEntity

@Database(
    entities = [ArticleEntity::class],
    version = DATABASE_VERSION,
    exportSchema = false
)

abstract class Database : RoomDatabase() {
    abstract val newsDao: NewsDao
}