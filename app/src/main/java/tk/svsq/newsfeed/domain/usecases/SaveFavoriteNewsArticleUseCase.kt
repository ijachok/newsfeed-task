package tk.svsq.newsfeed.domain.usecases

import tk.svsq.newsfeed.domain.models.Article
import tk.svsq.newsfeed.domain.repositories.NewsRepository
import javax.inject.Inject

class SaveFavoriteNewsArticleUseCase @Inject constructor(private val repository: NewsRepository) : UseCase<SaveFavoriteNewsArticleUseCase.Params, Unit>() {

    data class Params(val article: Article)

    override suspend fun run(): Result<Unit> {
        if (params == null) throw IllegalArgumentException("Parameters required")

        return params!!.run {
            repository.saveArticleToFavorites(article)
            Result.success(Unit)
        }
    }
}