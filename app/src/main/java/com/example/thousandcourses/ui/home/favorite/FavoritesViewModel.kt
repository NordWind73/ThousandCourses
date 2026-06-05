package com.example.thousandcourses.ui.home.favorite

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
class FavoritesViewModel @Inject constructor(
    private val getCoursesUseCase: GetCoursesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val userDataStore: UserDataStore,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _uiState = MutableStateFlow<FavoritesUiState>(FavoritesUiState.Loading)
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    private val currentUserEmail: String
        get() = userDataStore.getUserEmail()!!

    init {
        loadFavorites()
    }

    data class FavoriteCourseUi(
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

    fun loadFavorites() {
        viewModelScope.launch {
            _uiState.value = FavoritesUiState.Loading
            try {
                val allCourses = getCoursesUseCase()
                val favCourses = allCourses.filter { it.hasLike }

                val uiCourses = favCourses.map { course ->
                    FavoriteCourseUi(
                        id = course.id,
                        title = course.title,
                        text = course.text,
                        price = course.price,
                        rate = course.rate,
                        startDate = course.startDate,
                        publishDate = course.publishDate,
                        isFavorite = true,
                        imageRes = 0
                    )
                }
                _uiState.value = FavoritesUiState.Success(uiCourses)
            } catch (e: Exception) {
                 val message = e.message ?: context.getString(com.example.thousandcourses.R.string.error_loading_favorites)
                _uiState.value = FavoritesUiState.Error(message)
            }
        }
    }

    fun refreshFavorites() {
        loadFavorites()
    }

    fun toggleFavorite(courseId: String) {
        viewModelScope.launch {
            toggleFavoriteUseCase(courseId)
            loadFavorites()
        }
    }

    sealed class FavoritesUiState {
        data object Loading : FavoritesUiState()
        data class Success(val courses: List<FavoriteCourseUi>) : FavoritesUiState()
        data class Error(val message: String) : FavoritesUiState()
    }
}