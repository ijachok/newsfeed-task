package tk.svsq.newsfeed.presentation.news_details

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import tk.svsq.newsfeed.R
import tk.svsq.newsfeed.databinding.FragmentDetailsBinding
import tk.svsq.newsfeed.presentation.news_list.NewsListViewModel
import tk.svsq.newsfeed.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import tk.svsq.newsfeed.KEY_ARTICLE
import tk.svsq.newsfeed.domain.models.Article
import tk.svsq.newsfeed.presentation.common.extensions.*

@AndroidEntryPoint
class ArticleDetailsFragment : Fragment(R.layout.fragment_details) {

    private val viewModel: NewsListViewModel by viewModels()

    private val binding: FragmentDetailsBinding by viewBinding(FragmentDetailsBinding::bind)

    private var article: Article? = null

    private val client = object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            proceedIfResumed {
                binding.progressBar.isVisible = true
            }
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            proceedIfResumed {
                binding.progressBar.isGone = true
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            article = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(KEY_ARTICLE, Article::class.java)
            } else {
                it.get(KEY_ARTICLE) as? Article
            }
        }

        displayArticle()

        article?.let { viewModel.checkIsArticleSaved(it) }

        viewLifecycleOwner.observe {
            viewModel.isSaved.collectLatest {
                it?.let { saved ->
                    setupFloatingButton(saved)
                }
            }
        }
    }

    private fun setupFloatingButton(isSaved: Boolean) {
        proceedIfResumed {
            binding.fabSaveArticle.apply {
                article?.let { article ->
                    val message: String =
                        if (isSaved) {
                            setImageResource(R.drawable.ic_bookmark_selected)
                            getString(R.string.article_already_saved)
                        } else {
                            getString(R.string.article_saved)
                        }

                    setOnClickListener {
                        viewModel.saveArticleToFavorites(article)
                        it.showSnackbar(message)
                        setImageResource(R.drawable.ic_bookmark_selected)
                        viewModel.resetSaveState()
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()

        binding.progressBar.gone()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun displayArticle() {
        article?.url?.let {
            binding.webView.apply {
                webViewClient = client
                loadUrl(it)
                settings.javaScriptEnabled = true
            }
        } ?: run {
            "Error loading details for this article".showAsToast(context)
        }

    }
}