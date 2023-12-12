package tk.svsq.newsfeed.data.network.responses

import com.google.gson.annotations.SerializedName
import tk.svsq.newsfeed.domain.models.Article

data class NewsListResponse(
    @SerializedName("status")
    var status: String = "ok",
    @SerializedName("totalResults")
    var totalResults: Int = 0,
    @SerializedName("articles")
    var articles: List<Article> = listOf()
) {
    var errorMessage: String? = null
}
