package com.example.app_grupo7.network
import com.example.app_grupo7.model.ExternalPost
import retrofit2.Response
import retrofit2.http.GET

interface ExternalApiService {

    @GET("posts")
    suspend fun getPosts(): Response<List<ExternalPost>>
}