package com.example.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.favorites.FavoriteCourseDao
import com.example.data.favorites.FavoriteCourseEntity
import com.example.data.user.UserDao
import com.example.data.user.UserEntity

@Database(
    entities = [UserEntity::class,  FavoriteCourseEntity::class],
    version = 5,
    exportSchema = false,
)
abstract class CourseDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun favoriteCourseDao(): FavoriteCourseDao
}