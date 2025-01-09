package com.example.android_tuannq.list

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.android_tuannq.data.model.DetailUser
import com.example.android_tuannq.data.model.User
import com.example.android_tuannq.data.usecase.GetUsersUseCase
import com.karleinstein.basemvvm.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The [MainViewModel] is responsible for managing the data and logic for displaying a list of users and
 * fetching user details in the UI. It communicates with the [GetUsersUseCase] to fetch data and handles
 * errors, loading states, and state updates.
 *
 * @property getUsersUseCase The use case responsible for fetching the list of users and user details.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase
) : BaseViewModel() {

    private val _errorResponse: MutableStateFlow<Throwable?> = MutableStateFlow(null)
    val errorResponse: StateFlow<Throwable?> get() = _errorResponse

    private val _detailUser: MutableStateFlow<DetailUser?> = MutableStateFlow(null)
    val detailUser: StateFlow<DetailUser?> get() = _detailUser

    private val _listUser: MutableStateFlow<PagingData<User>?> = MutableStateFlow(null)
    val listUser: StateFlow<PagingData<User>?> get() = _listUser
    private val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("CoroutineExceptionHandler got $throwable")
        _errorResponse.value = throwable
    }

    fun getUsers() {
        viewModelScope.launch(handler) {
            getUsersUseCase.getUsersGitHub(this).catch {
                _errorResponse.value = it
            }.collect {
                _listUser.value = it
            }
        }
    }

    fun getDetail(userName: String) {
        viewModelScope.launch(handler) {
            getUsersUseCase.getDetailUser(userName).catch {
                _errorResponse.value = it
            }.onStart {

            }.collect {
                _detailUser.value = it
            }
        }
    }
}
