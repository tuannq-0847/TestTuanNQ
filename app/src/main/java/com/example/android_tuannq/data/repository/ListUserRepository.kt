package com.example.android_tuannq.data.repository

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.android_tuannq.data.local.AppDatabase
import com.example.android_tuannq.data.model.DetailUser
import com.example.android_tuannq.data.model.User
import com.example.android_tuannq.data.remote.ApiService
import com.example.android_tuannq.data.source.PagingGitMediator
import com.karleinstein.basemvvm.data.source.BaseRepositoryImpl
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

interface ListUserRepository {

    fun fetchUsers(scope: CoroutineScope): Flow<PagingData<User>>
    suspend fun getDetailUser(userName: String): Flow<DetailUser>
}

/**
 * Implementation of the [ListUserRepository] interface that interacts with the [ApiService] to fetch
 * users from the network and the [AppDatabase] to store and retrieve user data.
 * It utilizes the [PagingGitMediator] for loading paginated data from the network and the local database.
 *
 * @property context The [Context] of the application used for repository operations.
 * @property apiService The [ApiService] used to interact with the remote GitHub API to fetch user data.
 * @property database The [AppDatabase] used to store and retrieve user data locally.
 */
@OptIn(ExperimentalPagingApi::class)
class ListUserRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
    private val apiService: ApiService,
    private val database: AppDatabase
) : BaseRepositoryImpl(context), ListUserRepository {

    override fun fetchUsers(
        scope: CoroutineScope
    ): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = true, prefetchDistance = 0, initialLoadSize = 20),
            remoteMediator = PagingGitMediator(apiService, database)
        ) {
            database.userDao().getAll()
        }.flow.cachedIn(scope)
    }

    override suspend fun getDetailUser(userName: String): Flow<DetailUser> {
        return flowOf(apiService.getUserDetail(userName))
    }

}