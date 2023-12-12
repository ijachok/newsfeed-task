package tk.svsq.newsfeed.domain.usecases

import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import tk.svsq.newsfeed.domain.models.Article
import tk.svsq.newsfeed.domain.repositories.NewsRepository
import javax.inject.Inject

class GetPagedNewsUseCase @Inject constructor(var repository: NewsRepository)
    : FlowUseCase<PagingData<Article>, GetPagedNewsUseCase.Params>() {

    data class Params(val query: String, val scope: CoroutineScope)

    override suspend fun buildUseCase(params: Params): Flow<PagingData<Article>> {
        return repository.getPagedNews(params.query).cachedIn(params.scope)
    }
}