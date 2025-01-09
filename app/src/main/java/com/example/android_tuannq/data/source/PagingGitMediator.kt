package com.example.android_tuannq.data.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.android_tuannq.data.local.AppDatabase
import com.example.android_tuannq.data.model.RemoteKeys
import com.example.android_tuannq.data.model.User
import com.example.android_tuannq.data.remote.ApiService
import javax.inject.Inject

/**
 * A [RemoteMediator] responsible for loading data from a remote API and saving it to a local database.
 * This class handles pagination logic by determining the appropriate page number based on the load type
 * (refresh, prepend, or append), and it manages the remote keys for navigating through paginated data.
 * It interacts with the [ApiService] for fetching user data and the [AppDatabase] for storing data in the local
 * database, including remote keys to manage pagination.
 *
 * @property networkService The [ApiService] used to fetch data from the network.
 * @property database The [AppDatabase] used to store the fetched data and remote keys.
 */
@OptIn(ExperimentalPagingApi::class)
class PagingGitMediator @Inject constructor(
    private val networkService: ApiService,
    private val database: AppDatabase,
) : RemoteMediator<Int, User>() {

    /**
     * Loads data based on the current [LoadType] (refresh, prepend, or append) and updates the database with
     * the fetched data along with the appropriate remote keys for pagination.
     *
     * - For [LoadType.REFRESH], the method fetches new data and clears the existing data in the database.
     * - For [LoadType.PREPEND], the method fetches previous pages of data by using the remote keys.
     * - For [LoadType.APPEND], the method fetches subsequent pages of data based on the current remote key.
     *
     * This method handles the database transaction for inserting or clearing the data and remote keys.
     *
     * @param loadType The type of load operation (refresh, prepend, or append).
     * @param state The [PagingState] that contains the current paging configuration and state.
     * @return A [MediatorResult] that represents the result of the load operation, indicating whether the
     *         pagination has reached the end or if an error occurred.
     */
    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, User>
    ): MediatorResult {
        val dao = database.userDao()
        val since = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                nextKey
            }
        }

        try {
            val response = networkService.getUsers(since = since, page = state.config.pageSize)
            val endOfPaginationReached = response.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    dao.clearAll()
                    database.remoteKeysDao().clearRemoteKeys()
                }

                val keys = response.map { example ->
                    RemoteKeys(
                        id = example.id.orEmpty(),
                        prevKey = if (since == STARTING_PAGE_INDEX) null else since - 1,
                        nextKey = if (endOfPaginationReached) null else since + 1
                    )
                }
                database.remoteKeysDao().insertAll(keys)
                dao.insertAll(response)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, User>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { example -> database.remoteKeysDao().remoteKeysId(example.id) }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, User>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { example -> database.remoteKeysDao().remoteKeysId(example.id) }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, User>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().remoteKeysId(id)
            }
        }
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}