package tk.svsq.newsfeed.data.db.datasource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import tk.svsq.newsfeed.PAGE_SIZE
import tk.svsq.newsfeed.data.network.NewsApiService
import tk.svsq.newsfeed.data.network.responses.NewsListResponse
import tk.svsq.newsfeed.domain.models.Article


class NewsListModelPagingDataSource(private val api: NewsApiService, val query: String) : PagingSource<Int, Article>() {

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val pageNumber = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = api.getNewsByQueryAsync(page = pageNumber, query = query)
            val nextKey = if (response.articles.isEmpty()) null else pageNumber + (params.loadSize / PAGE_SIZE)
            LoadResult.Page(
                data = response.articles,
                prevKey = if (pageNumber == 1) null else nextKey,
                nextKey = nextKey
            )
        } catch (ex: Exception) {
            Log.e("NewsListModelPagingDataSource", "Error: ${ex.message}")
            LoadResult.Error(ex)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return null
    }
}