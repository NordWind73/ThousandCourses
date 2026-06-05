package com.example.data.favorites

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteCourseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteCourseEntity)

    @Query("DELETE FROM favorite_courses WHERE userEmail = :userEmail AND courseId = :courseId")
    suspend fun removeFavorite(userEmail: String, courseId: String)

    @Query("SELECT COUNT(*) FROM favorite_courses WHERE userEmail = :userEmail AND courseId = :courseId")
    suspend fun isCourseFavorite(userEmail: String, courseId: String): Boolean

    @Query("SELECT courseId FROM favorite_courses WHERE userEmail = :userEmail")
    suspend fun getFavoriteCourseIds(userEmail: String): List<String>
    @Query("DELETE FROM favorite_courses WHERE userEmail = :userEmail")
    suspend fun clearUserFavorites(userEmail: String)
}
