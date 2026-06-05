package com.example.data.repository

import com.example.data.api.CourseApi
import com.example.data.api.CourseApiService
import com.example.domain.model.Course
import com.example.domain.repository.CourseRepository
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CourseDataRepository @Inject constructor(
    private val apiService: CourseApiService
) : CourseRepository {

    private var cachedCourses: WeakReference<List<Course>> = WeakReference(null)

    override suspend fun getCourses(forceRefresh: Boolean): List<Course> {
        val cached = cachedCourses.get()
        if (!forceRefresh && cached != null) {
            return cached
        }
        return try {
            val response = apiService.getCourses()
            val courses = response.courses.map { it.toDomain() }
            cachedCourses = WeakReference(courses)
            courses
        } catch (e: Exception) {
            cached ?: emptyList()
        }
    }

    override fun clearCache() {
        cachedCourses.clear()
    }

    private fun CourseApi.toDomain() = Course(
        id = id,
        title = title,
        text = text,
        price = price,
        rate = rate,
        startDate = startDate,
        hasLike = false,
        publishDate = publishDate
    )
}