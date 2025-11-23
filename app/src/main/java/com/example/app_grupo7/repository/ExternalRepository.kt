package com.example.app_grupo7.repository


import com.example.app_grupo7.model.ExternalPost
import com.example.app_grupo7.network.ExternalApiService
import com.example.app_grupo7.network.ExternalRetrofitClient

class ExternalRepository {

    private val api = ExternalRetrofitClient.create(ExternalApiService::class.java)

    suspend fun getPosts(): Result<List<ExternalPost>> {
        return try {
            val response = api.getPosts()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
