package com.example.app_grupo7


import com.example.app_grupo7.model.ProductTCG
import com.example.app_grupo7.repository.ProductTCGRepository
import com.example.app_grupo7.viewmodel.ProductTCGViewModel
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
class ProductTCGViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var repo: ProductTCGRepository
    private lateinit var viewModel: ProductTCGViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repo = mockk()
        viewModel = ProductTCGViewModel(repo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadProducts actualiza lista correctamente`() = runTest {
        // GIVEN
        val mockList = listOf(
            ProductTCG(id = 1, nombre = "Producto A", descripcion = "Desc", precio = 1000)
        )
        coEvery { repo.getAllProducts() } returns Result.success(mockList)

        // WHEN
        viewModel.loadProducts()
        testScheduler.advanceUntilIdle()

        // THEN
        assertEquals(mockList, viewModel.products.value)
        assertFalse(viewModel.loading.value)
        assertNull(viewModel.error.value)
    }
}