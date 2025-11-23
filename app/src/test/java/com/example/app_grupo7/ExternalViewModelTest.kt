package com.example.app_grupo7


import com.example.app_grupo7.model.ExternalPost
import com.example.app_grupo7.repository.ExternalRepository
import com.example.app_grupo7.viewmodel.ExternalViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ExternalViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var repo: ExternalRepository
    private lateinit var vm: ExternalViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repo = mockk()
        vm = ExternalViewModel(repo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadPosts actualiza lista correctamente`() = runTest {
        val mockPosts = listOf(
            ExternalPost(userId = 1, id = 10, title = "Test", body = "Contenido")
        )

        coEvery { repo.getPosts() } returns Result.success(mockPosts)

        vm.loadPosts()
        testScheduler.advanceUntilIdle()

        assertEquals(mockPosts, vm.posts.value)
        assertFalse(vm.loading.value)
        assertNull(vm.error.value)
    }
}