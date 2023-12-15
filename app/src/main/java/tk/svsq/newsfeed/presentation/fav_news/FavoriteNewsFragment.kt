package tk.svsq.newsfeed.presentation.fav_news

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import tk.svsq.newsfeed.KEY_ARTICLE
import tk.svsq.newsfeed.R
import tk.svsq.newsfeed.databinding.FragmentFavoriteBinding
import tk.svsq.newsfeed.presentation.common.decoration.SpacingDecorationWithDivider
import tk.svsq.newsfeed.presentation.common.extensions.emptyString
import tk.svsq.newsfeed.presentation.common.extensions.observe
import tk.svsq.newsfeed.presentation.common.extensions.snackbarWithUndoAction
import tk.svsq.newsfeed.presentation.news_list.NewsArticlesAdapter
import tk.svsq.newsfeed.utils.viewbinding.viewBinding

@AndroidEntryPoint
class FavoriteNewsFragment : Fragment(R.layout.fragment_favorite) {

    private val viewModel: FavoriteNewsViewModel by viewModels()
    private val binding: FragmentFavoriteBinding by viewBinding(FragmentFavoriteBinding::bind)

    private val newsAdapter: NewsArticlesAdapter by lazy { NewsArticlesAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        observerArticles()
        applySwipeToDelete()

        viewModel.getSavedArticles()
    }

    private fun initRecyclerView() = binding.recyclerView.apply {

        newsAdapter.onArticleClicked = { article ->
            val bundle = bundleOf(KEY_ARTICLE to article)
            findNavController().navigate(R.id.global_action_navigate_to_detailsFragment, bundle)
        }
        adapter = newsAdapter
        addItemDecoration(SpacingDecorationWithDivider())

    }

    private fun observerArticles() {
        viewLifecycleOwner.observe {
            viewModel.articles.collectLatest {
                newsAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                runBlocking {
                    showContent(newsAdapter.itemCount >0, getString(R.string.no_favourites_found))

                }
            }
        }
    }

    private fun applySwipeToDelete() {
        val touchHelper =
            object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.bindingAdapterPosition
                    val currentItem = newsAdapter.getCurrentItem(position)
                    currentItem?.let {
                        viewModel.deleteArticle(it)
                        binding.root.snackbarWithUndoAction(
                            getString(R.string.article_deleted),
                            getString(R.string.undo)) {
                                viewModel.saveArticle(it)
                            }
                    }
                }
            }

        ItemTouchHelper(touchHelper).attachToRecyclerView(binding.recyclerView)
    }

    private fun showContent(contentAvailable: Boolean = false, message: String = emptyString()) {
        with(binding) {
            //recyclerView.isVisible = contentAvailable
            noFavouritesContent.isVisible = !contentAvailable
            if (!contentAvailable && message.isNotBlank()) {
                textNoFavourites.text = message
            }
        }
    }
}