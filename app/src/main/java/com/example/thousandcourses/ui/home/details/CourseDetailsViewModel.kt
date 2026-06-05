package com.example.thousandcourses.ui.home.details

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.R
import com.example.domain.repository.UserDataStore
import com.example.domain.usecase.GetCoursesUseCase
import com.example.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseDetailsViewModel @Inject constructor(
    private val getCoursesUseCase: GetCoursesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val userDataStore: UserDataStore,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    private val currentUserEmail: String
        get() = userDataStore.getUserEmail()!!

    var currentCourse: CourseDetailsUi? = null
        private set

    fun loadCourse(courseId: String) {
        viewModelScope.launch {
            _uiState.value = DetailsUiState.Loading
            try {
                val courses = getCoursesUseCase()
                val course = courses.find { it.id == courseId }

                if (course != null) {
                    val details = CourseDetailsUi(
                        id = course.id,
                        title = course.title,
                        fullDescription = course.text,
                        price = course.price,
                        rate = course.rate,
                        startDate = course.startDate,
                        publishDate = course.publishDate,
                        isFavorite = course.hasLike
                    )
                    currentCourse = details
                    _uiState.value = DetailsUiState.Success(details)
                } else {
                    _uiState.value = DetailsUiState.Error(context.getString(com.example.thousandcourses.R.string.course_not_found))
                }
            } catch (e: Exception) {
                val message = e.message ?: context.getString(com.example.thousandcourses.R.string.error_loading_courses)
                _uiState.value = DetailsUiState.Error(message)}
        }
    }

    fun toggleFavorite() {
        currentCourse?.let { course ->
            viewModelScope.launch {
                val newState = toggleFavoriteUseCase(course.id)
                val updated = course.copy(isFavorite = newState)
                currentCourse = updated
                _uiState.value = DetailsUiState.Success(updated)
            }
        }
    }
}