package tk.svsq.newsfeed.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "saved_article", indices = [Index(value = ["url"], unique = true)])
data class ArticleEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo("title")
    var title: String = "",
    @ColumnInfo("image_url")
    var imageUrl: String?,
    @ColumnInfo("description")
    var description: String?,
    @ColumnInfo("content")
    var content: String?,
    @ColumnInfo("author")
    var author: String?,
    @ColumnInfo("url")
    var url: String?,
    @ColumnInfo("publishedAt")
    var publishedAt: String?,
    @ColumnInfo("source_name")
    var sourceName: String?
)
