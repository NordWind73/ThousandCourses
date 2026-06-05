package com.example.thousandcourses.ui.home.details

data class CourseDetailsUi(
    val id: String,
    val title: String,
    val fullDescription: String,
    val price: String,
    val rate: String,
    val startDate: String,
    val publishDate: String,
    val isFavorite: Boolean
)