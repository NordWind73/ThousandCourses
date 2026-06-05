package com.example.thousandcourses.ui.home

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Success(val courses: List<CourseUi>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}