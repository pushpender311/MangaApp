package com.bittech.manga.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bittech.manga.local.AppDatabase
import com.bittech.manga.local.dao.MangaDao
import com.bittech.manga.local.dao.UserDao
import com.bittech.manga.network.ApiService
import com.bittech.manga.ui.auth.LoginRepository
import com.bittech.manga.ui.manga.MangaRepository
import com.bittech.manga.utils.AppPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
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
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "PupilMesh_db")
            .addMigrations(MIGRATION1_2).build()
    }

    val MIGRATION1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            super.migrate(db)
        }
    }

    @Singleton
    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()
    @Singleton
    @Provides
    fun provideMangaDao(db: AppDatabase): MangaDao = db.mangaDao()


    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://mangaverse-api.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("x-rapidapi-key", "60085583f1msh65d7da280b40685p1e1d6cjsn000838bf7853")
                    .addHeader("x-rapidapi-host", "mangaverse-api.p.rapidapi.com")
                    .build()
                chain.proceed(request)
            }.build()
    }

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): AppPreferences {
        return AppPreferences(context)

    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideLoginRepository(userDao: UserDao, appPreferences: AppPreferences): LoginRepository {
        return LoginRepository(userDao, appPreferences)
    }
    @Singleton
    @Provides
    fun provideMangaRepository(apiService: ApiService,mangaDao: MangaDao): MangaRepository{
        return MangaRepository(apiService,mangaDao)
    }
}