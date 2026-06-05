package com.example.thousandcourses.di

import android.content.Context
import androidx.room.Room
import com.example.data.BuildConfig
import com.example.data.CourseDatabase
import com.example.data.api.CourseApiService
import com.example.data.favorites.FavoriteCourseDao
import com.example.data.repository.CourseDataRepository
import com.example.data.repository.FavoriteRepositoryImpl
import com.example.data.repository.UserRepositoryImpl
import com.example.data.user.UserDao
import com.example.data.user.UserMailPref
import com.example.data.utils.PasswordHasher
import com.example.domain.repository.CourseRepository
import com.example.domain.repository.FavoriteRepository
import com.example.domain.repository.UserDataStore
import com.example.domain.repository.UserRepository
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CourseDatabase {
        return Room.databaseBuilder(
            context,
            CourseDatabase::class.java,
            "course_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: CourseDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        userDao: UserDao,
        passwordHasher: PasswordHasher
    ): UserRepository {
        return UserRepositoryImpl(userDao, passwordHasher)
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.base_url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideCourseApiService(retrofit: Retrofit): CourseApiService {
        return retrofit.create(CourseApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCourseRepository(
        apiService: CourseApiService
    ): CourseRepository {
        return CourseDataRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideFavoriteRepository(favoriteDao: FavoriteCourseDao): FavoriteRepository {
        return FavoriteRepositoryImpl(favoriteDao)
    }

    @Provides
    @Singleton
    fun provideUserMailPref(
        @ApplicationContext context: Context
    ): UserMailPref {
        return UserMailPref(context)
    }

    @Provides
    @Singleton
    fun provideFavoriteCourseDao(database: CourseDatabase): FavoriteCourseDao {
        return database.favoriteCourseDao()
    }

    @Provides
    @Singleton
    fun providePasswordHasher(): PasswordHasher {
        return PasswordHasher()
    }

    @Provides
    @Singleton
    fun provideUserDataStore(userMailPref: UserMailPref): UserDataStore {
        return userMailPref
    }
}