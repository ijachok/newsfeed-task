package tk.svsq.newsfeed.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tk.svsq.newsfeed.data.repositories.NewsRepositoryImpl
import tk.svsq.newsfeed.domain.repositories.NewsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNewsRepository(newsRepo: NewsRepositoryImpl): NewsRepository
}