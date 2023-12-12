package tk.svsq.newsfeed.data.db.dao

import androidx.room.*
import tk.svsq.newsfeed.data.db.entities.ArticleEntity

@Dao
interface NewsDao {
    @Query("SELECT * FROM saved_article")
    fun getNews(): List<ArticleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: ArticleEntity)

    @Query("DELETE FROM saved_article WHERE url = :url")
    suspend fun removeArticle(url: String)

    @Query("SELECT EXISTS (SELECT * FROM saved_article WHERE title = :title AND url = :url)")
    suspend fun checkIfExists(title: String, url: String): Boolean
}