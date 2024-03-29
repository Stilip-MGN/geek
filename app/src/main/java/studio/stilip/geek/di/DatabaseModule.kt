package studio.stilip.geek.di

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal class DatabaseModule {

    @Provides
    fun provideDatabase() =
        FirebaseDatabase.getInstance().reference

    @Provides
    fun provideStorage() =
        FirebaseStorage.getInstance().reference
}