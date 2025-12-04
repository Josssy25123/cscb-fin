package com.example.chatbotapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // For Android emulator talking to PC localhost:
    // use 10.0.2.2 instead of 127.0.0.1
    private const val BASE_URL = "http://10.5.0.2:8000/"

    val courseApi: CourseApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CourseApiService::class.java)
    }
}