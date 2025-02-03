package com.adista.finalproject.di

import android.content.Context
import android.location.Geocoder
import androidx.room.Room
import com.adista.finalproject.AddressHelper
import com.adista.finalproject.api.ApiAuthService
import com.adista.finalproject.api.ApiServiceProduct
import com.adista.finalproject.repository.DataProductRepo
import com.adista.finalproject.repository.ImplDataProductRepo // Assuming your implementation class is ImplDataProductRepo
import com.adista.finalproject.repository.FriendRepository
import com.adista.finalproject.repository.FriendRepositoryImpl
import com.adista.finalproject.database.FriendDao
import com.adista.finalproject.database.MyDatabase
import com.adista.finalproject.database.UserDao
import com.crocodic.core.data.CoreSession
import com.crocodic.core.helper.NetworkHelper
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideCoreSession(@ApplicationContext context: Context): CoreSession {
        return CoreSession(context)
    }

    @Singleton
    @Provides
    fun provideGeocoder(@ApplicationContext context: Context): Geocoder {
        return Geocoder(context, Locale.getDefault())
    }

    @Singleton
    @Provides
    fun provideAddressHelper(geocoder: Geocoder): AddressHelper {
        return AddressHelper(geocoder)
    }

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

    @Provides
    @Singleton
    fun provideUserDao(database: MyDatabase): UserDao {
        return database.userDao()
    }

    @Singleton
    @Provides
    fun provideFriendRepository(friendDao: FriendDao): FriendRepository = FriendRepositoryImpl(friendDao)

    @Singleton
    @Provides
    fun provideApiService(): ApiServiceProduct {
        return NetworkHelper.provideApiService(
            baseUrl = "https://dummyjson.com/",
            okHttpClient = NetworkHelper.provideOkHttpClient(),
            converterFactory = listOf(GsonConverterFactory.create())
        )
    }

    @Singleton
    @Provides
    fun provideDataProductRepo(apiServiceProduct: ApiServiceProduct): DataProductRepo =
        ImplDataProductRepo(apiServiceProduct)

    @Singleton
    @Provides
    fun providesOkHttp(session: CoreSession): OkHttpClient {

        return NetworkHelper.provideOkHttpClient().newBuilder().apply {
            addInterceptor {
                val original = it.request()
                val requestBuilder = original.newBuilder()
                    .header("Content-Type", "application/json")
                    .method(original.method, original.body)

                val token = session.getString(CoreSession.PREF_UID)
                if (token.isNotEmpty()) {
                    requestBuilder.header("Authorization", "Bearer $token")
                }

                val request = requestBuilder.build()
                it.proceed(request)
            }
        }.build()
    }

    @Singleton
    @Provides
    fun provideApiAuthService(okHttpClient: OkHttpClient): ApiAuthService {
        return NetworkHelper.provideApiService(
            baseUrl = "https://kelas-industri.crocodic.net/rubben/Shoppku/public/api/v1/",
            okHttpClient = okHttpClient,
            converterFactory = listOf(GsonConverterFactory.create())
        )
    }


}