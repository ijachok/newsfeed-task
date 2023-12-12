package tk.svsq.newsfeed.presentation.news_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tk.svsq.newsfeed.domain.models.Article
import tk.svsq.newsfeed.databinding.ListItemArticleBinding
import tk.svsq.newsfeed.presentation.common.extensions.toDateTimeString
import tk.svsq.newsfeed.utils.glide.GlideHelper.loadImage

class NewsArticlesAdapter : PagingDataAdapter<Article, NewsArticlesAdapter.NewsViewHolder>(ARTICLE_COMPARATOR) {

    companion object {
        private val ARTICLE_COMPARATOR = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.title == newItem.title && oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }
        }
    }

    var onArticleClicked: ((article: Article) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsArticlesAdapter.NewsViewHolder {
        val binding =
            ListItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsArticlesAdapter.NewsViewHolder, position: Int) {
        getItem(position)?.let { holder.bindData(it) }
    }

    fun getCurrentItem(position: Int) = getItem(position)

    inner class NewsViewHolder(private val binding: ListItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val currentArticle = getItem(position)
                    if (currentArticle != null) onArticleClicked?.invoke(currentArticle)
                }
            }
        }

        fun bindData(article: Article) {
            binding.apply {
                tvArticleTitle.text = article.title
                tvArticleSource.text = article.source?.name
                tvArticleDescription.text = article.description
                tvArticlePublishedAt.text = article.publishedAt?.toDateTimeString()

                loadImage(root.context, article.imageUrl, ivArticleImage)
            }
        }
    }
}