package tk.svsq.newsfeed.data.repositories

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import tk.svsq.newsfeed.MAX_SIZE
import tk.svsq.newsfeed.PAGE_SIZE
import tk.svsq.newsfeed.data.db.Database
import tk.svsq.newsfeed.data.db.datasource.NewsListModelPagingDataSource
import tk.svsq.newsfeed.data.mappers.ArticleDBMapper
import tk.svsq.newsfeed.data.network.NewsApiService
import tk.svsq.newsfeed.domain.models.Article
import tk.svsq.newsfeed.domain.repositories.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val apiService: NewsApiService,
    private val database: Database) : NewsRepository {

    private val mapper = ArticleDBMapper()

    override suspend fun getPagedNews(query: String): Flow<PagingData<Article>> {
        return Pager(
            config = generatePagingConfig(),
        ) {
            NewsListModelPagingDataSource(apiService, query)
        }.flow.flowOn(Dispatchers.IO)
    }

    override suspend fun getAllFavoriteNewsFromLocal(): List<Article> =
        mapper.reverse(database.newsDao.getNews())

    override suspend fun saveArticleToFavorites(article: Article) {
        database.newsDao.insert(mapper.map(article))
    }

    override suspend fun removeArticleFromFavorites(article: Article) {
        article.url?.let {
            database.newsDao.removeArticle(it)
        }
    }

    private fun generatePagingConfig() = PagingConfig(
        maxSize = MAX_SIZE,
        pageSize = PAGE_SIZE,
        enablePlaceholders = false
    )

    override suspend fun checkIfArticleExists(article: Article) = database.newsDao.checkIfExists(article.title.orEmpty(), article.url.orEmpty())
}