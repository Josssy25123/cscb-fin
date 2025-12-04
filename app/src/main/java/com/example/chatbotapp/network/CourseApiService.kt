package com.example.chatbotapp.network

import retrofit2.http.*
import retrofit2.Response

data class CourseDto(
    val id: Int,
    val code: String,
    val name: String,
    val credits: Int,
    val prerequisites: String,
    val offered: String,
    val category: String? // can be null
)

data class CourseQueryRequest(
    val question: String
)

data class CourseQueryResponse(
    val answer: String,
    val courses: List<CourseDto>
)

// --- Retrofit interface ---

interface CourseApiService {

    @GET("courses")
    suspend fun getCourses(
        @Query("category") category: String? = null
    ): Response<List<CourseDto>>

    @GET("courses/search")
    suspend fun searchCourses(
        @Query("q") query: String,
        @Query("category") category: String? = null
    ): Response<List<CourseDto>>

    @POST("courses/query")
    suspend fun queryCourses(
        @Body body: CourseQueryRequest
    ): Response<CourseQueryResponse>
}