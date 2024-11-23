package com.adista.finalproject.di

import android.content.Context
import androidx.room.Room
import com.adista.finalproject.repository.FriendRepository
import com.adista.finalproject.repository.FriendRepositoryImpl
import com.adista.finalproject.database.FriendDao
import com.adista.finalproject.database.MyDatabase
import com.adista.finalproject.session.CoreSession
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
    fun providesDatabase(@ApplicationContext context: Context): MyDatabase {
        return Room.databaseBuilder(context, MyDatabase::class.java, "my_database")
            .fallbackToDestructiveMigration()
            .build()
    }
    @Singleton
    @Provides
    fun provideFriendDao(database: MyDatabase): FriendDao = database.friendDao()

    @Singleton
    @Provides
    fun provideFriendRepository(friendDao: FriendDao): FriendRepository = FriendRepositoryImpl(friendDao)
}