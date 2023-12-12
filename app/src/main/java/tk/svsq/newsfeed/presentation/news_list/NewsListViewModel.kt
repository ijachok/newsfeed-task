package tk.svsq.newsfeed.presentation.news_list

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import tk.svsq.newsfeed.domain.models.Article
import tk.svsq.newsfeed.domain.usecases.CheckIfArticleExistsUseCase
import tk.svsq.newsfeed.domain.usecases.GetPagedNewsUseCase
import tk.svsq.newsfeed.domain.usecases.RemoveFavoriteNewsArticleUseCase
import tk.svsq.newsfeed.domain.usecases.SaveFavoriteNewsArticleUseCase
import tk.svsq.newsfeed.presentation.common.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor() : BaseViewModel() {

    private val _articles = MutableStateFlow<PagingData<Article>>(PagingData.empty())
    private val _currentQuery = MutableStateFlow("")
    val articles = _articles.asStateFlow()
    val currentQuery = _currentQuery.asStateFlow()

    val isSaved = MutableStateFlow<Boolean?>(null)

    @Inject lateinit var getPagedNewsUseCase: GetPagedNewsUseCase
    @Inject lateinit var saveFavoriteNewsArticleUseCase: SaveFavoriteNewsArticleUseCase
    @Inject lateinit var removeFavoriteNewsArticleUseCase: RemoveFavoriteNewsArticleUseCase
    @Inject lateinit var checkIfArticleExistsUseCase: CheckIfArticleExistsUseCase

    fun getArticles(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getPagedNewsUseCase.execute(
                params = GetPagedNewsUseCase.Params(query, this),
                onSuccess = {
                    _articles.value = it
                },
                onFailure = {
                    handleException(it)
                }
            )
        }
    }

    fun saveArticleToFavorites(article: Article) {
        saveFavoriteNewsArticleUseCase(params = SaveFavoriteNewsArticleUseCase.Params(article)) { }
    }

    fun checkIsArticleSaved(article: Article) {
        return checkIfArticleExistsUseCase(params = CheckIfArticleExistsUseCase.Params(article)) {
            it.onSuccess { saved ->
                isSaved.value = saved
            }
        }
    }

    fun resetSaveState() { isSaved.value = null }
}