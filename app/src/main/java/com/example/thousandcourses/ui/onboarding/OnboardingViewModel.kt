package com.example.thousandcourses.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thousandcourses.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor() : ViewModel() {

    private val _categories = MutableStateFlow(
        listOf(
            CategoryUi(id = 1, nameRes = R.string.category_admin),
            CategoryUi(id = 2, nameRes = R.string.category_traffic),
            CategoryUi(id = 3, nameRes = R.string.category_marketing),
            CategoryUi(id = 4, nameRes = R.string.category_b28_marketing)
        )
    )
    val categories: StateFlow<List<CategoryUi>> = _categories.asStateFlow()

    private val _selectedCount = MutableStateFlow(0)
    val selectedCount: StateFlow<Int> = _selectedCount.asStateFlow()


    fun toggleCategory(categoryId: Int) {
        val current = _categories.value
        val updated = current.map { category ->
            if (category.id == categoryId) {
                category.copy(isSelected = !category.isSelected)
            } else {
                category
            }
        }

        _categories.value = updated
        _selectedCount.value = updated.count { it.isSelected }
    }

    fun getSelectedIds(): List<Int> {
        return _categories.value
            .filter { it.isSelected }
            .map { it.id }
    }
}

data class CategoryUi(
    val id: Int,
    val nameRes: Int,
    val isSelected: Boolean = false
)