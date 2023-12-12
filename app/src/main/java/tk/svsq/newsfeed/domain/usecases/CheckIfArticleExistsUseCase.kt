package tk.svsq.newsfeed.domain.usecases

import tk.svsq.newsfeed.domain.models.Article
import tk.svsq.newsfeed.domain.repositories.NewsRepository
import javax.inject.Inject

class CheckIfArticleExistsUseCase @Inject constructor(private val repository: NewsRepository) : UseCase<CheckIfArticleExistsUseCase.Params, Boolean>() {

    data class Params(val article: Article)

    override suspend fun run(): Result<Boolean> {

        if (params == null) throw IllegalArgumentException("Parameters required")

        return params!!.run {
            Result.success(repository.checkIfArticleExists(article))
        }
    }
}