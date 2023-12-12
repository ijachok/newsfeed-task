package tk.svsq.newsfeed.domain.usecases

import tk.svsq.newsfeed.domain.models.Article
import tk.svsq.newsfeed.domain.repositories.NewsRepository
import javax.inject.Inject

class GetFavoriteLocalNewsUseCase @Inject constructor(private val repository: NewsRepository) : UseCase<UseCase.None, List<Article>>() {

    override suspend fun run(): Result<List<Article>> {

        val list = repository.getAllFavoriteNewsFromLocal().sortedByDescending { it.publishedAt }
        return Result.success(list)
    }
}