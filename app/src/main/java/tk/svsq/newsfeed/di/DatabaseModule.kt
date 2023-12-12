package tk.svsq.newsfeed.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import tk.svsq.newsfeed.DATABASE_NAME
import tk.svsq.newsfeed.data.db.Database
import tk.svsq.newsfeed.data.db.dao.NewsDao
import tk.svsq.newsfeed.data.repositories.NewsRepositoryImpl
import tk.svsq.newsfeed.domain.repositories.NewsRepository
import tk.svsq.newsfeed.domain.usecases.GetFavoriteLocalNewsUseCase
import tk.svsq.newsfeed.domain.usecases.GetPagedNewsUseCase
import tk.svsq.newsfeed.domain.usecases.RemoveFavoriteNewsArticleUseCase
import tk.svsq.newsfeed.domain.usecases.SaveFavoriteNewsArticleUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): Database =
        Room.databaseBuilder(context, Database::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideNewsDao(db: Database): NewsDao = db.newsDao
}