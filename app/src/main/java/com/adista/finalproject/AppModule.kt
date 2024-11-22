package com.adista.finalproject

import android.content.Context
import com.adista.finalproject.database.FriendDao
import com.adista.finalproject.database.MyDatabase
import com.crocodic.core.CoreSession
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCoreSession(@ApplicationContext context: Context): CoreSession = CoreSession(context)

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext appContext: Context): MyDatabase {
        return MyDatabase.getDatabase(appContext)
    }
    @Singleton
    @Provides
    fun provideFriendDao(database: MyDatabase): FriendDao = database.friendDao()

    @Singleton
    @Provides
    fun provideFriendRepository(friendDao: FriendDao): FriendRepository = FriendRepositoryImpl(friendDao)
}