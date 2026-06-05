package com.example.domain.usecase

import com.example.domain.model.Course
import com.example.domain.repository.CourseRepository
import com.example.domain.repository.FavoriteRepository
import com.example.domain.repository.UserDataStore
import javax.inject.Inject

class GetCoursesUseCase @Inject constructor(
    private val courseRepository: CourseRepository,
    private val favoriteRepository: FavoriteRepository,
    private val userDataStore: UserDataStore
) {
    suspend operator fun invoke(forceRefresh: Boolean = false): List<Course> {
        val email = userDataStore.getUserEmail() ?: ""
        val courses = courseRepository.getCourses(forceRefresh)
        val favIds = favoriteRepository.getFavoriteCourseIds(email)
        return courses.map { it.copy(hasLike = it.id in favIds) }
    }
}