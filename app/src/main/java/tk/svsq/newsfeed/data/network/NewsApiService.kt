package tk.svsq.newsfeed.data.network

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query
import tk.svsq.newsfeed.API_VERSION
import tk.svsq.newsfeed.BuildConfig
import tk.svsq.newsfeed.PAGE_SIZE
import tk.svsq.newsfeed.data.network.responses.NewsListResponse

interface NewsApiService {
    @GET("/$API_VERSION/everything")
    suspend fun getNewsByQueryAsync(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int = PAGE_SIZE,
        @Query("q") query: String = "",
        @Query("sortBy") sortedBy: String = "publishedAt",
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY
    ): NewsListResponse
}