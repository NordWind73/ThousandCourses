package com.example.data.api
data class CourseApi(
    val id: String,
    val title: String,
    val text: String,
    val price: String,
    val rate: String,
    val startDate: String,
    val hasLike: Boolean = false,
    val publishDate: String
)
