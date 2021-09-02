package com.henryudorji.theater.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.henryudorji.theater.data.local.db.TheaterDao
import com.henryudorji.theater.data.local.db.TheaterDb
import com.henryudorji.theater.data.remote.ServiceApi
import com.henryudorji.theater.data.repository.MovieRepository
import com.henryudorji.theater.utils.NetworkManager
import com.henryudorji.theater.utils.NetworkUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofitInstance(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(ServiceApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun providePokeDexApi(retrofit: Retrofit): ServiceApi =
        retrofit.create(ServiceApi::class.java)

    @Provides
    @Singleton
    fun provideRepository(
        serviceApi: ServiceApi,
        theaterDb: TheaterDb
    ) = MovieRepository(serviceApi)

    @Provides
    @Singleton
    fun  provideNetworkManager(@ApplicationContext context: Context) = NetworkManager(context)
}