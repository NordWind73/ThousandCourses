package com.example.thousandcourses.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thousandcourses.R
import com.example.domain.model.Course
import com.example.domain.repository.UserDataStore
import com.example.domain.usecase.GetCoursesUseCase
import com.example.domain.usecase.SortCoursesUseCase
import com.example.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getCoursesUseCase: GetCoursesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val userDataStore: UserDataStore,
    private val sortCoursesUseCase: SortCoursesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val currentUserEmail: String
        get() = userDataStore.getUserEmail()!!

    private var allCourses: List<Course> = emptyList()
    private var isSortingEnabled: Boolean = false
    private var isDescending: Boolean = true
    private var searchQuery: String = ""

    init {
        loadCourses()
    }

    fun loadCourses(forceRefresh: Boolean = false) {
        val email = userDataStore.getUserEmail()
        if (email.isNullOrBlank()) {
            _uiState.value = HomeUiState.Error(context.getString(R.string.not_authorized))
            return
        }
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                allCourses = getCoursesUseCase(forceRefresh)
                applyFilterAndSort()
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: context.getString(R.string.unknown_error))
            }
        }
    }

    fun refreshCourses() {
        loadCourses(forceRefresh = true)
    }

    fun toggleSort() {
        if (!isSortingEnabled) {
            isSortingEnabled = true
            isDescending = true
        } else {
            isDescending = !isDescending
        }
        applyFilterAndSort()
    }
    fun filterCourses(query: String) {
        searchQuery = query
        applyFilterAndSort()
    }
    private fun applyFilterAndSort() {
        var result = allCourses
        if (searchQuery.isNotBlank()) {
            result = result.filter { course ->
                course.title.contains(searchQuery, ignoreCase = true) ||
                        course.text.contains(searchQuery, ignoreCase = true)
            }
        }
        if (isSortingEnabled) {
            result = sortCoursesUseCase(result, isDescending)
        }
        val uiCourses = result.map { it.toUi() }
        _uiState.value = HomeUiState.Success(uiCourses)
    }

    fun toggleFavorite(courseId: String) {
        viewModelScope.launch {
            val newState = toggleFavoriteUseCase(courseId)
            allCourses = allCourses.map {
                if (it.id == courseId) it.copy(hasLike = newState) else it
            }
            applyFilterAndSort()
        }
    }

    private fun Course.toUi() = CourseUi(
        id = id,
        title = title,
        text = text,
        price = price,
        rate = rate,
        startDate = startDate,
        publishDate = publishDate,
        isFavorite = hasLike,
        imageRes = 0
    )
}