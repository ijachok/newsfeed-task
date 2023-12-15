package tk.svsq.newsfeed.presentation.news_list

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import tk.svsq.newsfeed.KEYWORD_ANDROID
import tk.svsq.newsfeed.KEY_ARTICLE
import tk.svsq.newsfeed.R
import tk.svsq.newsfeed.databinding.FragmentNewsBinding
import tk.svsq.newsfeed.presentation.common.PagingLoadStateAdapter
import tk.svsq.newsfeed.presentation.common.decoration.SpacingDecorationWithDivider
import tk.svsq.newsfeed.presentation.common.extensions.emptyString
import tk.svsq.newsfeed.presentation.common.extensions.isNetworkAvailable
import tk.svsq.newsfeed.presentation.common.extensions.observe
import tk.svsq.newsfeed.presentation.common.extensions.showAsToast
import tk.svsq.newsfeed.utils.viewbinding.viewBinding

@AndroidEntryPoint
class NewsListFragment : Fragment(R.layout.fragment_news) {

    private val viewModel: NewsListViewModel by viewModels()
    private val binding: FragmentNewsBinding by viewBinding(FragmentNewsBinding::bind)

    private val newsArticlesAdapter: NewsArticlesAdapter by lazy { NewsArticlesAdapter() }

    private val loadStateAdapter = PagingLoadStateAdapter { newsArticlesAdapter.retry() }

    private var searchView: SearchView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeData()
        handleRetryOption()
        setupMenu()

        viewModel.getArticles(query = KEYWORD_ANDROID) // Default keyword
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_search, menu)

                val searchItem = menu.findItem(R.id.action_search_news)
                searchView = searchItem.actionView as SearchView

                val pendingQuery = viewModel.currentQuery.value
                if (pendingQuery.isNotBlank()) {
                    searchItem.expandActionView()
                    searchView?.setQuery(pendingQuery, true)
                }

                searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean =
                        if (query != null) {
                            searchView?.clearFocus()
                            searchForArticles(query)
                            true
                        } else false

                    override fun onQueryTextChange(newText: String?): Boolean = false
                })


            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menuSortAll -> { viewModel.getArticles(query = KEYWORD_ANDROID) }
                    R.id.menuSortBusiness -> { searchForArticles("business") }
                    R.id.menuSortEntertainment -> { searchForArticles("entertainment") }
                    R.id.menuSortGeneral -> { searchForArticles("general") }
                    R.id.menuSortHealth -> { searchForArticles("health") }
                    R.id.menuSortScience -> { searchForArticles("science") }
                    R.id.menuSortSports -> { searchForArticles("sports") }
                    R.id.menuSortTechnology -> { searchForArticles("technology") }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun searchForArticles(newText: String) {
        binding.recyclerView.scrollToPosition(0)
        viewModel.getArticles(newText)
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            addItemDecoration(SpacingDecorationWithDivider())
            newsArticlesAdapter.onArticleClicked = { article ->
                val bundle = bundleOf(KEY_ARTICLE to article)
                findNavController().navigate(R.id.global_action_navigate_to_detailsFragment, bundle)
            }
            adapter = newsArticlesAdapter.withLoadStateFooter(
                footer = loadStateAdapter
            )
        }
    }

    private fun observeData() {
        viewLifecycleOwner.observe {
            viewModel.articles.collectLatest {
                showContent(newsArticlesAdapter.itemCount > 0)
                newsArticlesAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
        }

        viewLifecycleOwner.observe {
            viewModel.errorStateFlow.collectLatest {
                if (newsArticlesAdapter.itemCount > 0) {
                    getString(R.string.msg_error_fetching_news).showAsToast(context)
                } else {
                    showContent(false, getString(R.string.msg_error_load_news, it))
                }
            }
        }
    }

    private fun handleRetryOption() {
        newsArticlesAdapter.addLoadStateListener { loadState ->
            if (!requireContext().isNetworkAvailable() && newsArticlesAdapter.itemCount < 1) {
                showContent(message = getString(R.string.no_network_connection))
                //return@addLoadStateListener
            }

            with(binding) {
                val isRefresh = loadState.source.refresh
                // Empty state
                if (isRefresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && newsArticlesAdapter.itemCount < 1) {
                    showContent(message = getString(R.string.no_news_found))
                } else {
                    showContent(newsArticlesAdapter.itemCount > 0)
                }
                when (isRefresh) {
                    is LoadState.Loading -> {
                        retryContent.isGone = true
                        binding.progressBar.isVisible = true
                    }

                    is LoadState.NotLoading -> {
                        showContent(newsArticlesAdapter.itemCount > 0)
                    }

                    is LoadState.Error -> {
                        val state = loadState.source.refresh as LoadState.Error
                        showContent(
                            false,
                            getString(R.string.msg_error_load_news, state.error.message.orEmpty())
                        )
                    }
                }
                buttonRetry.setOnClickListener {
                    newsArticlesAdapter.retry()
                }
                swipeToRefresh.apply {
                    setOnRefreshListener {
                        isRefreshing = true
                        newsArticlesAdapter.retry()
                        isRefreshing = false
                    }
                }
            }
        }
    }

    private fun showContent(contentAvailable: Boolean = false, message: String = emptyString()) {
        with(binding) {
            recyclerView.isVisible = contentAvailable
            retryContent.isVisible = !contentAvailable
            if (!contentAvailable && message.isNotBlank()) {
                textViewError.text = message
            }
            progressBar.isGone = true
        }
    }
}