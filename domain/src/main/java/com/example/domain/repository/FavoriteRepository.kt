package com.example.domain.repository

interface FavoriteRepository {
    suspend fun toggleFavorite(userEmail: String, courseId: String): Boolean
    suspend fun isFavorite(userEmail: String, courseId: String): Boolean
    suspend fun getFavoriteCourseIds(userEmail: String): List<String>
    suspend fun clearUserFavorites(userEmail: String)
}