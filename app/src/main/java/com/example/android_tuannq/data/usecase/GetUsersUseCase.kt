package com.example.android_tuannq.data.usecase

import androidx.paging.PagingData
import com.example.android_tuannq.data.model.DetailUser
import com.example.android_tuannq.data.model.User
import com.example.android_tuannq.data.repository.ListUserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetUsersUseCase {
    fun getUsersGitHub(scope: CoroutineScope): Flow<PagingData<User>>
    suspend fun getDetailUser(userName: String): Flow<DetailUser>
}

/**
 * Implementation of the [GetUsersUseCase] interface that interacts with the [ListUserRepository] to
 * fetch users and retrieve detailed user information.
 *
 * @property repository The [ListUserRepository] that handles data operations, such as fetching users
 * and getting user details from the network or local storage.
 */
class GetUserUseCaseImpl @Inject constructor(private val repository: ListUserRepository) :
    GetUsersUseCase {
    override fun getUsersGitHub(scope: CoroutineScope) = repository.fetchUsers(scope)
    override suspend fun getDetailUser(userName: String): Flow<DetailUser> =
        repository.getDetailUser(userName)
}
