package com.example.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface CourseApiService {
    @GET("u/0/uc")
    suspend fun getCourses(
        @Query("id")
        id: String = "15arTK7XT2b7Yv4BJsmDctA4Hg-BbS8-q",
        @Query("value")
        value: String = "export"
    ): CourseApiResponse
}
