package com.example.app_grupo7.network

import com.example.app_grupo7.model.ProductTCG
import retrofit2.Response
import retrofit2.http.*

interface ProductTCGApiService {

    @GET("api/productos")
    suspend fun getAllProducts(): Response<List<ProductTCG>>

    @GET("api/productos/{id}")
    suspend fun getProductById(@Path("id") id: Long): Response<ProductTCG>

    @POST("api/productos")
    suspend fun createProduct(@Body product: ProductTCG): Response<ProductTCG>

    @PUT("api/productos/{id}")
    suspend fun updateProduct(
        @Path("id") id: Long,
        @Body product: ProductTCG
    ): Response<ProductTCG>

    @DELETE("api/productos/{id}")
    suspend fun deleteProduct(@Path("id") id: Long): Response<Void>
}