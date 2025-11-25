package com.example.app_grupo7.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // IP del Backend
    private const val BASE_URL = "http://13.219.43.161:8080/"
    // Para utilizar JSON COMO FORMATO PARA TRABAJAR
    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun <T> createService(service: Class<T>): T {
        return retrofit.create(service)
    }
}
