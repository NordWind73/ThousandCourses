package com.example.domain.repository

import com.example.domain.model.Course

interface CourseRepository {
    suspend fun getCourses(forceRefresh: Boolean): List<Course>
    fun clearCache()
}