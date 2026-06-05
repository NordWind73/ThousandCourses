package com.example.domain.usecase

import com.example.domain.model.Course
import javax.inject.Inject

class SortCoursesUseCase @Inject constructor() {
    operator fun invoke(courses: List<Course>, isDescending: Boolean): List<Course> {
        return if (isDescending) {
            courses.sortedByDescending { it.publishDate }
        } else {
            courses.sortedBy { it.publishDate }
        }
    }
}