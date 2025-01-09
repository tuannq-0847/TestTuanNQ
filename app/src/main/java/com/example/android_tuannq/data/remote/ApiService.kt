package com.example.android_tuannq.data.remote

import com.example.android_tuannq.data.model.DetailUser
import com.example.android_tuannq.data.model.User
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    suspend fun getUsers(@Query("per_page") page: Int, @Query("since") since: Int): List<User>

    @GET("users/{login_username}")
    suspend fun getUserDetail(@Path("login_username") userName: String): DetailUser
}