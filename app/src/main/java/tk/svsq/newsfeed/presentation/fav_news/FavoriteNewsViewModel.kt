package tk.svsq.newsfeed.presentation.fav_news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import tk.svsq.newsfeed.domain.models.Article
import tk.svsq.newsfeed.domain.usecases.GetFavoriteLocalNewsUseCase
import tk.svsq.newsfeed.domain.usecases.RemoveFavoriteNewsArticleUseCase
import tk.svsq.newsfeed.domain.usecases.SaveFavoriteNewsArticleUseCase
import javax.inject.Inject

@HiltViewModel
class FavoriteNewsViewModel @Inject constructor() : ViewModel() {

    private val _articles = MutableSharedFlow<PagingData<Article>>()
    val articles = _articles.asSharedFlow()

    @Inject lateinit var getFavoriteLocalNewsUseCase: GetFavoriteLocalNewsUseCase
    @Inject lateinit var saveFavoriteNewsArticleUseCase: SaveFavoriteNewsArticleUseCase
    @Inject lateinit var removeFavoriteNewsArticleUseCase: RemoveFavoriteNewsArticleUseCase

    fun getSavedArticles() {
        getFavoriteLocalNewsUseCase {
            it.onSuccess {
                viewModelScope.launch {
                    _articles.emit(PagingData.from(it))
                }
            }
        }
    }

    fun deleteArticle(article: Article) {
        removeFavoriteNewsArticleUseCase(params = RemoveFavoriteNewsArticleUseCase.Params(article)) {
            it.onSuccess {
                getSavedArticles()
            }
        }
    }

    fun saveArticle(article: Article) {
        viewModelScope.launch {
            saveFavoriteNewsArticleUseCase(params = SaveFavoriteNewsArticleUseCase.Params(article)) {
                it.onSuccess {
                    getSavedArticles()
                }
            }

        }
    }
}