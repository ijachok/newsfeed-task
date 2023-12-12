package tk.svsq.newsfeed.domain.repositories

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import tk.svsq.newsfeed.domain.models.Article

interface NewsRepository {

    suspend fun getPagedNews(query: String): Flow<PagingData<Article>>

    suspend fun getAllFavoriteNewsFromLocal(): List<Article>

    suspend fun saveArticleToFavorites(article: Article)

    suspend fun removeArticleFromFavorites(article: Article)

    suspend fun checkIfArticleExists(article: Article): Boolean
}