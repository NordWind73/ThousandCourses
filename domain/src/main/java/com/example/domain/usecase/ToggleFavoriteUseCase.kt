package com.example.domain.usecase

import com.example.domain.repository.FavoriteRepository
import com.example.domain.repository.UserDataStore
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val userDataStore: UserDataStore
) {
    suspend operator fun invoke(courseId: String): Boolean {
        val email = userDataStore.getUserEmail() ?: return false
        return favoriteRepository.toggleFavorite(email, courseId)
    }
}