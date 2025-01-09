package com.example.android_tuannq.di

import android.content.Context
import androidx.room.Room
import com.example.android_tuannq.data.local.AppDatabase
import com.example.android_tuannq.data.remote.ApiService
import com.example.android_tuannq.data.repository.ListUserRepository
import com.example.android_tuannq.data.repository.ListUserRepositoryImpl
import com.example.android_tuannq.data.usecase.GetUserUseCaseImpl
import com.example.android_tuannq.data.usecase.GetUsersUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideApiService(): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        return Retrofit.Builder().baseUrl("https://api.github.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ApiService::class.java)
    }

    @Provides
    fun provideUserRepository(
        @ApplicationContext context: Context, apiService: ApiService, appDatabase: AppDatabase
    ): ListUserRepository {
        return ListUserRepositoryImpl(context, apiService, appDatabase)
    }

    @Provides
    fun provideFetchUsersUseCase(userRepository: ListUserRepository): GetUsersUseCase {
        return GetUserUseCaseImpl(userRepository)
    }

    @Provides
    fun provideRoomDB(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context, AppDatabase::class.java, DB_NAME
        ).build()
    }

    const val DB_NAME = "database-tuannq-demo"
}
