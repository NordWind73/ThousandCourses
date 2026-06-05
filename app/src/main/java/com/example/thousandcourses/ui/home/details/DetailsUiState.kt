package com.example.thousandcourses.ui.home.details

sealed class DetailsUiState {
    data object Loading : DetailsUiState()
    data class Success(val course: CourseDetailsUi) : DetailsUiState()
    data class Error(val message: String) : DetailsUiState()
}