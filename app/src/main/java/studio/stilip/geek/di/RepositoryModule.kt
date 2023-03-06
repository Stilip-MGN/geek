package studio.stilip.geek.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import studio.stilip.geek.data.repositories.GameRepositoryImpl
import studio.stilip.geek.data.repositories.UserRepositoryImpl
import studio.stilip.geek.domain.repository_interface.GameRepository
import studio.stilip.geek.domain.repository_interface.UserRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideAdRepository(repositoryImpl: GameRepositoryImpl): GameRepository

    @Binds
    abstract fun provideUserRepository(repositoryImpl: UserRepositoryImpl): UserRepository

}