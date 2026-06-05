package com.example.thousandcourses.ui.home

data class CourseUi(
    val id: String,
    val title: String,
    val text: String,
    val price: String,
    val rate: String,
    val startDate: String,
    val publishDate: String,
    val isFavorite: Boolean,
    val imageRes: Int
)