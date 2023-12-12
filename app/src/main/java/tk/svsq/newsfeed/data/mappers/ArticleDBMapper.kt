package tk.svsq.newsfeed.data.mappers

import tk.svsq.newsfeed.data.db.entities.ArticleEntity
import tk.svsq.newsfeed.domain.models.Article
import tk.svsq.newsfeed.domain.models.Source

class ArticleDBMapper : Mapper<Article, ArticleEntity>() {
    override fun map(from: Article): ArticleEntity {
        return ArticleEntity(
            id = 0,
            title = from.title.orEmpty(),
            imageUrl = from.imageUrl,
            description = from.description,
            content = from.content,
            author = from.author,
            url = from.url,
            publishedAt = from.publishedAt,
            sourceName = from.source?.name
        )
    }

    override fun reverse(to: ArticleEntity): Article {
        return Article(
            source = Source(to.sourceName, to.sourceName),
            author = to.author,
            title = to.title,
            imageUrl = to.imageUrl,
            description = to.description,
            url = to.url,
            publishedAt = to.publishedAt,
            content = to.content
        )
    }
}