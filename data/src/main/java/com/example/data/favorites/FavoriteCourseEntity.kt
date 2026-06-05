package com.example.data.favorites

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorite_courses",
    indices = [Index(value = ["userEmail", "courseId"], unique = true)]
)
data class FavoriteCourseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userEmail: String,
    val courseId: String,
)
