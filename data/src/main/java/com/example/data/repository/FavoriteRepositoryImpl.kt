package com.example.data.repository


import com.example.data.favorites.FavoriteCourseDao
import com.example.data.favorites.FavoriteCourseEntity
import com.example.domain.repository.FavoriteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteCourseDao
) : FavoriteRepository {

    override suspend fun toggleFavorite(userEmail: String, courseId: String): Boolean {
        return if (isFavorite(userEmail, courseId)) {
            removeFromFavorites(userEmail, courseId)
            false
        } else {
            addToFavorites(userEmail, courseId)
            true
        }
    }

    override suspend fun isFavorite(userEmail: String, courseId: String): Boolean {
        return favoriteDao.isCourseFavorite(userEmail, courseId)
    }

    override suspend fun getFavoriteCourseIds(userEmail: String): List<String> {
        return favoriteDao.getFavoriteCourseIds(userEmail)
    }

    override suspend fun clearUserFavorites(userEmail: String) {
        favoriteDao.clearUserFavorites(userEmail)
    }

    private suspend fun addToFavorites(userEmail: String, courseId: String) {
        favoriteDao.addFavorite(FavoriteCourseEntity(userEmail = userEmail, courseId = courseId))
    }

    private suspend fun removeFromFavorites(userEmail: String, courseId: String) {
        favoriteDao.removeFavorite(userEmail, courseId)
    }
}
