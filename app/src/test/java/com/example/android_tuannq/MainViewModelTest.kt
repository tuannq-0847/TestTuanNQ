package com.example.android_tuannq

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.example.android_tuannq.data.model.DetailUser
import com.example.android_tuannq.data.model.User
import com.example.android_tuannq.data.usecase.GetUsersUseCase
import com.example.android_tuannq.list.MainViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private val getUsersUseCase: GetUsersUseCase = mockk()
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        // Set the Main dispatcher to the test dispatcher for testing
        Dispatchers.setMain(testDispatcher)
        // Initialize the ViewModel
        viewModel = MainViewModel(getUsersUseCase)
    }

    @After
    fun tearDown() {
        // Reset the main dispatcher to the original one after the test
        Dispatchers.resetMain()
    }

    @Test
    fun `getUsers should update listUser when successful`() = runTest {
        // Arrange
        val mockPagingData = PagingData.from(listOf(User()))
        coEvery { getUsersUseCase.getUsersGitHub(any()) } returns flow {
            emit(mockPagingData)
        }

        // Act
        viewModel.getUsers()
        delay(200)
        // Assert
        assertEquals(mockPagingData, viewModel.listUser.value)
    }

    @Test
    fun `getUsers should update errorResponse when exception occurs`() = runTest {
        // Arrange
        val error = Throwable("Network Error")
        coEvery { getUsersUseCase.getUsersGitHub(any()) } throws error

        // Act
        viewModel.getUsers()

        // Assert
        delay(200)
        assertNotNull(viewModel.errorResponse.value)
        assertEquals(error, viewModel.errorResponse.value)
    }

    @Test
    fun `getDetail should update detailUser when successful`() = runTest {
        // Arrange
        val mockDetailUser = DetailUser("testUser")
        coEvery { getUsersUseCase.getDetailUser(any()) } returns flow {
            emit(mockDetailUser)
        }

        // Act
        viewModel.getDetail("testUser")

        // Assert
        delay(200)
        assertNotNull(viewModel.detailUser.value)
        assertEquals(mockDetailUser, viewModel.detailUser.value)
    }

    @Test
    fun `getDetail should update errorResponse when exception occurs`() = runTest {
        // Arrange
        val error = Throwable("Network Error")
        coEvery { getUsersUseCase.getDetailUser(any()) } throws error

        // Act
        viewModel.getDetail("testUser")

        // Assert
        delay(200)
        assertEquals(error, viewModel.errorResponse.value)
    }
}
