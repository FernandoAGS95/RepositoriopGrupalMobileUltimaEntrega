package com.example.app_grupo7.repository

import com.example.app_grupo7.model.ProductTCG
import com.example.app_grupo7.network.ProductTCGApiService
import com.example.app_grupo7.network.RetrofitClient

class ProductTCGRepository {

    private val api = RetrofitClient.createService(ProductTCGApiService::class.java)

    suspend fun getAllProducts(): Result<List<ProductTCG>> {
        return try {
            val response = api.getAllProducts()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProductById(id: Long): Result<ProductTCG> {
        return try {
            val response = api.getProductById(id)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}